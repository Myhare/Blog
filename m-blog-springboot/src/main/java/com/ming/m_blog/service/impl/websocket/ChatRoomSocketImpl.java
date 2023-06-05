package com.ming.m_blog.service.impl.websocket;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ming.m_blog.dto.chatRecord.ChatMessageDTO;
import com.ming.m_blog.dto.chatRecord.ChatRecordDTO;
import com.ming.m_blog.dto.chatRecord.RecallMessageDTO;
import com.ming.m_blog.mapper.ChatRecordMapper;
import com.ming.m_blog.pojo.ChatRecord;
import com.ming.m_blog.utils.IpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.server.ServerEndpointConfig;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArraySet;

import static com.ming.m_blog.enums.ChatTypeEnum.*;

/**
 * 聊天室Websocket连接
 */
/*
 * @ServerEndpoint 注解是一个类层次的注解，它的功能主要是将目前的类定义成一个websocket服务器端,
 * 注解的值将被用于监听用户连接的终端访问URL地址,客户端可以通过这个URL来连接到WebSocket服务器端
 */
@Service
@ServerEndpoint(value = "/chatRoom",configurator = ChatRoomSocketImpl.ChatConfigurator.class)    // 连接路径-->ws://localhost:8080/chatRoom
public class ChatRoomSocketImpl {

    /*
        每一个客户端都有一个ChatRoomSocketImpl对象与之对应
        客户端可以通过Session对象获取发送数据对象，并且把数据发送给指定的客户端
     */

    /**
     * 使用一个静态的线程安全set将各个连接对象保存起来
     */
    private static CopyOnWriteArraySet<ChatRoomSocketImpl> chatRoomUserSet = new CopyOnWriteArraySet<>();

    /**
     * session对象，每个对象都通过session对象发送消息
     */
    private Session session;

    /**
     * 声明httpSession对象，之前在这里面存储了用户名
     */
    private HttpSession httpSession;

    private static ChatRecordMapper chatRecordMapper;
    @Autowired
    public void setChatRecordService(ChatRecordMapper chatRecordMapper) {
        ChatRoomSocketImpl.chatRecordMapper = chatRecordMapper;
    }


    /**
     * 获取连接的ip地址
     * TODO 了解一下为什么使用static
     */
    public static class ChatConfigurator extends ServerEndpointConfig.Configurator {
        private static final String IP_HEAD_KEY = "X-Real-IP";

        /**
         * 当创建一个新的连接的时候调用
         * @param sec 每个连接都有一个独立的ServerEndpointConfig实例，在这个实例中存储连接相关的配置和用户属性
         */
        @Override
        public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
            try {
                // 将客户端的ip地址存到sec的UserProperties中
                // System.out.println("----成功进入modifyHandshake----");
                // Map<String, List<String>> headers = request.getHeaders();
                // headers.keySet().forEach(key -> {
                //     System.out.println("key : "+key+"--->"+"value : "+headers.get(key));
                // });
                List<String> list = request.getHeaders().get(IP_HEAD_KEY.toLowerCase());
                list.forEach(System.out::println);
                sec.getUserProperties().put(IP_HEAD_KEY,list.get(0));
            } catch (Exception e) {
                sec.getUserProperties().put(IP_HEAD_KEY,"未知IP");
            }
        }
    }

    // 连接事件
    @OnOpen
    public void onOpen(Session session, EndpointConfig endpointConfig) throws IOException {
        System.out.println("成功有客户端连接进来服务端");
        System.out.println(session);
        this.session = session;
        // 将个人信息添加到人员集合中
        chatRoomUserSet.add(this);
        // 获取消息历史记录
        ChatRecordDTO chatRecordDTO = listChartRecords(endpointConfig);
        // 向客户端发送消息历史记录
        ChatMessageDTO chatMessageDTO = ChatMessageDTO.builder()
                .type(HISTORY_RECORD.getType())
                .data(chatRecordDTO)
                .build();
        // 对当前接入连接的用户session发送历史聊天记录
        session.getBasicRemote().sendText(JSON.toJSONString(chatMessageDTO));
        // 更新在线人数
        updateOnlineCount();
    }

    /**
     * 接收客户端消息
     * @param session 客户端的session
     * @param message 发送的消息，这里是一个JSON字符串
     */
    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
        ChatMessageDTO chatMessageDTO = JSON.parseObject(message,ChatMessageDTO.class);
        // 将消息存入历史聊天记录中
        // 将消息广播到其他所有session中
        switch (Objects.requireNonNull(getChatType(chatMessageDTO.getType()))){
            case HEART_BEAT:        // 心跳消息
                break;
            case SEND_MESSAGE:      // 发送消息
                // 广播消息
                // 将chatMessageDTO中的data中的message转化成ChatRecord对象
                ChatRecord chatRecord = JSON.parseObject(JSON.toJSONString(chatMessageDTO.getData()), ChatRecord.class); // 强转会报错
                chatMessageDTO.setData(chatRecord);
                // 将消息存入历史消息记录中
                chatRecordMapper.insert(chatRecord);
                // 广播消息
                broadcastMessage(chatMessageDTO);
                break;
            case RECALL_MESSAGE:    // 撤回消息
                // 删除历史聊天记录
                RecallMessageDTO recallMessageDTO = JSON.parseObject(JSON.toJSONString(chatMessageDTO.getData()), RecallMessageDTO.class);
                chatRecordMapper.deleteById(recallMessageDTO.getId());
                // 广播消息
                broadcastMessage(chatMessageDTO);
                break;
            default:
                break;
        }
    }

    // 关闭事件
    @OnClose
    public void onClose(Session session) throws IOException {
        System.out.println("有一个客户端关闭了连接");
        chatRoomUserSet.removeIf(chatRoomSocket -> session.equals(chatRoomSocket.session));
        updateOnlineCount();
    }

    // 更新在线人数
    private void updateOnlineCount() throws IOException {
        int onlineSize = chatRoomUserSet.size();
        ChatMessageDTO data = ChatMessageDTO.builder()
                .type(ONLINE_COUNT.getType())
                .data(onlineSize)
                .build();
        broadcastMessage(data);
    }

    /**
     * 加载历史记录
     */
    private ChatRecordDTO listChartRecords(EndpointConfig endpointConfig){
        // 获取ip地址
        String ipAddress = endpointConfig.getUserProperties().get(ChatConfigurator.IP_HEAD_KEY).toString();
        System.out.println("获取到的ip地址:"+ipAddress);
        // 查询前12小时的历史消息
        List<ChatRecord> chatRecordList = chatRecordMapper.selectList(new LambdaQueryWrapper<ChatRecord>()
                .ge(ChatRecord::getCreateTime, DateUtil.offsetHour(new Date(), -12))
        );

        return ChatRecordDTO
                .builder()
                .chatRecordList(chatRecordList)
                .ipAddress(ipAddress)
                .ipSource(IpUtils.getIpSource(ipAddress))
                .build();
    }

    /**
     * 广播消息
     * @param chatMessageDTO 要发送的消息
     */
    public void broadcastMessage(ChatMessageDTO chatMessageDTO) throws IOException {
        // 对所有用户都发送消息
        for (ChatRoomSocketImpl chatRoomSocket : chatRoomUserSet) {
            System.out.println(JSON.toJSONString(chatMessageDTO));
            chatRoomSocket.session.getBasicRemote().sendText(JSON.toJSONString(chatMessageDTO));
        }
    }

}

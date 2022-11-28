package com.ming.m_blog.dto;

import lombok.*;

/**
 * 聊天消息类
 */
@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageDTO {

    /**
     * 消息类型
     */
    private Integer type;

    /**
     * 消息内容
     */
    private Object data;

}

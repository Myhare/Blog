package com.ming.m_blog.hander;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ming.m_blog.constant.RedisPrefixConst;
import com.ming.m_blog.dto.UserDetailDTO;
import com.ming.m_blog.dto.UserInfoDTO;
import com.ming.m_blog.mapper.UserAuthMapper;
import com.ming.m_blog.mapper.UserInfoMapper;
import com.ming.m_blog.pojo.UserAuth;
import com.ming.m_blog.service.RedisService;
import com.ming.m_blog.utils.BeanCopyUtils;
import com.ming.m_blog.utils.IpUtils;
import com.ming.m_blog.utils.JwtUtil;
import com.ming.m_blog.utils.UserUtils;
import com.ming.m_blog.vo.ResponseResult;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

import static com.ming.m_blog.constant.CommonConst.APPLICATION_JSON;

/**
 * 认证成功处理
 */
@Component
public class AuthenticationSuccessHandlerImpl implements AuthenticationSuccessHandler {

    @Autowired
    private UserAuthMapper userAuthMapper;
    @Autowired
    private RedisService redisService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse, Authentication authentication) throws IOException {

        // 获取当前登录用户返回登录信息
        // 判断是前台登录还是后台登录
        boolean isFront = Boolean.parseBoolean(httpServletRequest.getParameter("isFront"));
        // 更新用户基本信息
        updateUserInfo(httpServletRequest);
        // System.out.println(isFront);
        // 判断是前台还是后台登录，因为前端的验证逻辑不同，这里返回给前端的信息也不同
        if (isFront){
            // 前台登录
            UserInfoDTO userLoginDTO = BeanCopyUtils.copyObject(UserUtils.getLoginUser(), UserInfoDTO.class);
            httpServletResponse.setContentType(APPLICATION_JSON);
            httpServletResponse.getWriter().write(JSON.toJSONString(ResponseResult.ok(userLoginDTO)));
        }else {
            // 后台登录将用户id封装成token返回给前端
            Integer userId = UserUtils.getLoginUserId();
            // token对用户的id进行加密
            String token = createToken(userId);
            // 将token存到redis中
            redisService.set(RedisPrefixConst.BACKSTAGE_LOGIN_TOKEN+userId,token,60*60);
            httpServletResponse.setContentType(APPLICATION_JSON);
            httpServletResponse.getWriter().write(JSON.toJSONString(ResponseResult.ok(token))); // 将token返回给前端
        }
    }

    /**
     * 通过用户id获取token
     * @param userId 用户id
     * @return token
     */
    private String createToken(Integer userId){
        return JwtUtil.createJWT(Integer.toString(userId));
    }


    /**
     * 更新登录用户信息
     * @param request 用户发起的请求
     */
    private void updateUserInfo(HttpServletRequest request){
        UserDetailDTO loginUser = UserUtils.getLoginUser();

        // 获取用户ip地址
        String ipAddress = IpUtils.getIpAddress(request);
        // 获取ip来源
        String ipSource = IpUtils.getIpSource(ipAddress);
        // 获取设备信息
        UserAgent userAgent = IpUtils.getUserAgent(request);
        String browser = userAgent.getBrowser().getName();      // 浏览器
        String os = userAgent.getOperatingSystem().getName();   // 操作系统

        // 更新SpringSecurity中UserDetails中的用户登录设备信息,因为是引用类型这里直接修改就行
        loginUser.setIpAddress(ipAddress);
        loginUser.setIpSource(ipSource);
        loginUser.setBrowser(browser);
        loginUser.setOs(os);

        // 更新数据库中用户的信息
        Integer userId = loginUser.getUserId();
        Date loginTime = loginUser.getLoginTime();
        UserAuth userAuth = UserAuth.builder()
                .id(userId)
                .ipAddress(ipAddress)
                .ipSource(ipSource)
                .loginTime(loginTime)
                .build();
        userAuth.setLoginTime(loginTime);
        userAuthMapper.updateById(userAuth);
    }

}















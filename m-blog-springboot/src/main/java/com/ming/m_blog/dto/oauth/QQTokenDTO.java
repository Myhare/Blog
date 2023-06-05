package com.ming.m_blog.dto.oauth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * qq token信息
 *
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QQTokenDTO {

    /**
     * openId为对当前应用进行授权的QQ用户的身份识别码
     */
    private String openid;

    /**
     * 客户端id
     */
    private String client_id;

}

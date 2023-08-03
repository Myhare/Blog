package com.ming.m_blog.dto.oauth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 社交账号用户信息
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SocialUserInfoDTO {

    /**
     * id
     */
    private String id;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像
     */
    private String avatar;
}

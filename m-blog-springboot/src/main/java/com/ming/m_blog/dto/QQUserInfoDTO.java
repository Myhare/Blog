package com.ming.m_blog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * qq用户信息dto
 *
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QQUserInfoDTO {

    /**
     * 用户在QQ空间的昵称
     */
    private String nickname;

    /**
     * 大小为40*40的qq头像
     */
    private String figureurl_qq_1;


}

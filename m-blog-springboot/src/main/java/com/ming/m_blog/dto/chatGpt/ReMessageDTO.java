package com.ming.m_blog.dto.chatGpt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 返回给前端的数据对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReMessageDTO {

    /**
     * 角色 system、user或assistant
     */
    private String role;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 姓名（可选）
     */
    private String name;

    /**
     * 用户头像
     */
    private String avatar;

}

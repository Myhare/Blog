package com.ming.m_blog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 发送邮件DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailSendDTO implements Serializable {

    /**
     * 要发送的email
     */
    private String email;

    /**
     * 邮件主题
     */
    private String subject;

    /**
     * 邮件内容
     */
    private String content;

}

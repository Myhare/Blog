package com.ming.m_blog.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户地区DTO
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAreaDTO {

    /**
     * 地区名
     */
    private String name;

    /**
     * 数量
     */
    private Long value;

}

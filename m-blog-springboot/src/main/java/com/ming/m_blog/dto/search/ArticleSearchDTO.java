package com.ming.m_blog.dto.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * es搜搜文章对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "article")
public class ArticleSearchDTO {

    /**
     * 文章id
     */
    @Id
    private Integer id;

    /**
     * 文章标题
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String title;

    /**
     * 文章内容
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String content;

    /**
     * 是否删除
     */
    @Field(type = FieldType.Integer)
    private Integer isDelete;

    /**
     * 文章状态
     */
    @Field(type = FieldType.Integer)
    private Integer status;

}

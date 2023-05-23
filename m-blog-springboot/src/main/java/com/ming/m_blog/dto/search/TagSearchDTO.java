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
 * 搜索标签结果
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "tag")
public class TagSearchDTO {

    /**
     * 标签id
     */
    @Id
    private Integer id;

    /**
     * 标签名称，这里标签不使用分词器
     */
    @Field(type = FieldType.Text, analyzer = "keyword")
    private String tagName;

}

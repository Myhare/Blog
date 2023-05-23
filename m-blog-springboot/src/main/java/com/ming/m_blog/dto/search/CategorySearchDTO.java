package com.ming.m_blog.dto.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "category")
public class CategorySearchDTO {

    /**
     * 分类id
     */
    @Id
    private Integer id;

    /**
     * 分类名称
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String categoryName;

}

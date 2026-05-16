package com.example.knowledgebase.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleVersion {

    private Long id;
    private Long articleId;
    private String title;
    private String content;
    private Integer version;
    private String publishedBy;
    private LocalDateTime publishedAt;
}
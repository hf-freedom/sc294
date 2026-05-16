package com.example.knowledgebase.dto.response;

import com.example.knowledgebase.model.Article.ArticleStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleResponse {
    private Long id;
    private String title;
    private String content;
    private String category;
    private String authorId;
    private String reviewerId;
    private ArticleStatus status;
    private Integer version;
    private Double score;
    private Integer viewCount;
    private Boolean isHot;
    private Boolean referencedByTicket;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime publishedAt;
}
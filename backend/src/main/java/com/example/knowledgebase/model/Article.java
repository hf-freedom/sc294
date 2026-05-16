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
public class Article {

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
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime publishedAt;
    private LocalDateTime expiresAt;

    public enum ArticleStatus {
        DRAFT, PENDING_REVIEW, UNDER_REVIEW, APPROVED, PUBLISHED, REJECTED, OPTIMIZE
    }
}
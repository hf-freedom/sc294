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
public class OptimizeTask {

    private Long id;
    private Long articleId;
    private String articleTitle;
    private Double currentScore;
    private String reason;
    private TaskStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;

    public enum TaskStatus {
        PENDING, PROCESSING, COMPLETED
    }
}
package com.example.knowledgebase.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewStats {

    private Long id;
    private LocalDate date;
    private Integer pendingReviewCount;
    private Integer expiredCount;
    private Integer lowQualityCount;
    private LocalDateTime createdAt;
}
package com.example.knowledgebase.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewStatsResponse {
    private LocalDate date;
    private Integer pendingReviewCount;
    private Integer expiredCount;
    private Integer lowQualityCount;
}
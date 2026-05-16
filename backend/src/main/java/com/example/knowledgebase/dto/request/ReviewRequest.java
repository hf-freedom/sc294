package com.example.knowledgebase.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequest {
    private Long articleId;
    private String reviewerId;
    private Boolean approved;
    private String comment;
}
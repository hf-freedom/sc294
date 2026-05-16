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
public class Ticket {

    private Long id;
    private String title;
    private String description;
    private Long articleId;
    private String creatorId;
    private LocalDateTime createdAt;
}
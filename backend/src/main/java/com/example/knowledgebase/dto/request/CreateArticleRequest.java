package com.example.knowledgebase.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateArticleRequest {
    private String title;
    private String content;
    private String category;
    private String authorId;
}
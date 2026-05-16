package com.example.knowledgebase.controller;

import com.example.knowledgebase.dto.request.ReviewRequest;
import com.example.knowledgebase.dto.response.ApiResponse;
import com.example.knowledgebase.model.Article;
import com.example.knowledgebase.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/pending")
    public ResponseEntity<ApiResponse<List<Article>>> getPendingReviews(
            @RequestParam(required = false) String reviewerId) {
        List<Article> articles;
        if (reviewerId != null) {
            articles = reviewService.getPendingReviews(reviewerId);
        } else {
            articles = reviewService.getAllPendingReviews();
        }
        return ResponseEntity.ok(ApiResponse.success(articles));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Article>> reviewArticle(@RequestBody ReviewRequest request) {
        try {
            Article article = reviewService.reviewArticle(request);
            String message = request.getApproved() ? "Article approved" : "Article rejected";
            return ResponseEntity.ok(ApiResponse.success(message, article));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
}
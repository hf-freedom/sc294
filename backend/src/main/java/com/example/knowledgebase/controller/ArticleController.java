package com.example.knowledgebase.controller;

import com.example.knowledgebase.dto.request.CreateArticleRequest;
import com.example.knowledgebase.dto.request.UpdateArticleRequest;
import com.example.knowledgebase.dto.response.ApiResponse;
import com.example.knowledgebase.dto.response.ArticleResponse;
import com.example.knowledgebase.model.Article;
import com.example.knowledgebase.model.Article.ArticleStatus;
import com.example.knowledgebase.model.ArticleVersion;
import com.example.knowledgebase.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @PostMapping
    public ResponseEntity<ApiResponse<ArticleResponse>> createArticle(@RequestBody CreateArticleRequest request) {
        ArticleResponse response = articleService.createArticle(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Article created successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ArticleResponse>>> getAllArticles(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String category) {
        List<ArticleResponse> articles;
        if (status != null) {
            articles = articleService.getArticlesByStatus(ArticleStatus.valueOf(status));
        } else if (category != null) {
            articles = articleService.getArticlesByCategory(category);
        } else {
            articles = articleService.getAllArticles();
        }
        return ResponseEntity.ok(ApiResponse.success(articles));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ArticleResponse>> getArticle(@PathVariable Long id) {
        ArticleResponse response = articleService.getArticle(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ArticleResponse>> updateArticle(
            @PathVariable Long id,
            @RequestBody UpdateArticleRequest request) {
        ArticleResponse response = articleService.updateArticle(id, request);
        return ResponseEntity.ok(ApiResponse.success("Article updated successfully", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteArticle(@PathVariable Long id) {
        try {
            articleService.deleteArticle(id);
            return ResponseEntity.ok(ApiResponse.success("Article deleted successfully", null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/{id}/submit")
    public ResponseEntity<ApiResponse<ArticleResponse>> submitForReview(@PathVariable Long id) {
        ArticleResponse response = articleService.submitForReview(id);
        return ResponseEntity.ok(ApiResponse.success("Article submitted for review", response));
    }

    @PostMapping("/{id}/publish")
    public ResponseEntity<ApiResponse<ArticleResponse>> publishArticle(
            @PathVariable Long id,
            @RequestParam String reviewerId) {
        try {
            ArticleResponse response = articleService.publishArticle(id, reviewerId);
            return ResponseEntity.ok(ApiResponse.success("Article published successfully", response));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/{id}/rollback/{version}")
    public ResponseEntity<ApiResponse<ArticleResponse>> rollbackToVersion(
            @PathVariable Long id,
            @PathVariable Integer version) {
        try {
            ArticleResponse response = articleService.rollbackToVersion(id, version);
            return ResponseEntity.ok(ApiResponse.success("Article rolled back successfully", response));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/{id}/versions")
    public ResponseEntity<ApiResponse<List<ArticleVersion>>> getArticleVersions(@PathVariable Long id) {
        List<ArticleVersion> versions = articleService.getArticleVersions(id);
        return ResponseEntity.ok(ApiResponse.success(versions));
    }
}
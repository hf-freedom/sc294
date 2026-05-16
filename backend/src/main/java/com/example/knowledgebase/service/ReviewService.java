package com.example.knowledgebase.service;

import com.example.knowledgebase.dto.request.ReviewRequest;
import com.example.knowledgebase.model.Article;
import com.example.knowledgebase.model.Article.ArticleStatus;
import com.example.knowledgebase.model.User;
import com.example.knowledgebase.model.User.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {

    private final InMemoryStorage storage;
    private final Random random = new Random();

    public String assignReviewer(String category) {
        List<User> reviewers = storage.getUsersByRoleAndCategory(UserRole.REVIEWER, category);
        
        if (reviewers.isEmpty()) {
            reviewers = storage.getUsersByRole(UserRole.REVIEWER);
        }

        if (reviewers.isEmpty()) {
            throw new RuntimeException("No reviewers available");
        }

        User reviewer = reviewers.get(random.nextInt(reviewers.size()));
        return reviewer.getId().toString();
    }

    public Article reviewArticle(ReviewRequest request) {
        Article article = storage.getArticle(request.getArticleId());
        if (article == null) {
            throw new RuntimeException("Article not found");
        }

        User reviewer = storage.getUser(Long.parseLong(request.getReviewerId()));
        if (reviewer == null) {
            throw new RuntimeException("Reviewer not found");
        }

        if (Boolean.TRUE.equals(article.getIsHot())) {
            if (reviewer.getRole() != UserRole.SENIOR_REVIEWER) {
                throw new RuntimeException("Hot articles require senior reviewer approval");
            }
        }

        article.setStatus(request.getApproved() ? ArticleStatus.APPROVED : ArticleStatus.REJECTED);
        article.setUpdatedAt(LocalDateTime.now());

        if (!request.getApproved()) {
            article.setReviewerId(null);
        }

        storage.saveArticle(article);
        log.info("Article {} reviewed by {}: {}", request.getArticleId(), request.getReviewerId(), 
                request.getApproved() ? "APPROVED" : "REJECTED");
        return article;
    }

    public List<Article> getPendingReviews(String reviewerId) {
        return storage.getArticlesByStatus(ArticleStatus.PENDING_REVIEW).stream()
                .filter(a -> reviewerId.equals(a.getReviewerId()))
                .collect(java.util.stream.Collectors.toList());
    }

    public List<Article> getAllPendingReviews() {
        return storage.getArticlesByStatus(ArticleStatus.PENDING_REVIEW);
    }
}
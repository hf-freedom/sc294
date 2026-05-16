package com.example.knowledgebase.service;

import com.example.knowledgebase.dto.response.ReviewStatsResponse;
import com.example.knowledgebase.model.Article.ArticleStatus;
import com.example.knowledgebase.model.ReviewStats;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatsService {

    private final InMemoryStorage storage;

    private static final double LOW_QUALITY_THRESHOLD = 3.0;

    public ReviewStatsResponse generateDailyStats() {
        LocalDate today = LocalDate.now();
        LocalDateTime now = LocalDateTime.now();

        List<com.example.knowledgebase.model.Article> pendingArticles = 
                storage.getArticlesByStatus(ArticleStatus.PENDING_REVIEW);
        List<com.example.knowledgebase.model.Article> expiredArticles = 
                storage.getExpiredArticles(now);
        List<com.example.knowledgebase.model.Article> lowQualityArticles = 
                storage.getArticlesByScoreLessThan(LOW_QUALITY_THRESHOLD);

        ReviewStats existing = storage.getReviewStatsByDate(today);
        
        ReviewStats stats = ReviewStats.builder()
                .date(today)
                .pendingReviewCount(pendingArticles.size())
                .expiredCount(expiredArticles.size())
                .lowQualityCount(lowQualityArticles.size())
                .createdAt(LocalDateTime.now())
                .build();

        if (existing != null) {
            stats.setId(existing.getId());
        }

        storage.saveReviewStats(stats);
        log.info("Generated daily stats: pending={}, expired={}, lowQuality={}", 
                pendingArticles.size(), expiredArticles.size(), lowQualityArticles.size());

        return toResponse(stats);
    }

    public ReviewStatsResponse getTodayStats() {
        ReviewStats stats = storage.getTodayReviewStats();
        if (stats == null) {
            return ReviewStatsResponse.builder()
                    .date(LocalDate.now())
                    .pendingReviewCount(0)
                    .expiredCount(0)
                    .lowQualityCount(0)
                    .build();
        }
        return toResponse(stats);
    }

    private ReviewStatsResponse toResponse(ReviewStats stats) {
        return ReviewStatsResponse.builder()
                .date(stats.getDate())
                .pendingReviewCount(stats.getPendingReviewCount())
                .expiredCount(stats.getExpiredCount())
                .lowQualityCount(stats.getLowQualityCount())
                .build();
    }
}
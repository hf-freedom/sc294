package com.example.knowledgebase.service;

import com.example.knowledgebase.dto.request.CreateArticleRequest;
import com.example.knowledgebase.dto.request.UpdateArticleRequest;
import com.example.knowledgebase.dto.response.ArticleResponse;
import com.example.knowledgebase.model.Article;
import com.example.knowledgebase.model.Article.ArticleStatus;
import com.example.knowledgebase.model.ArticleVersion;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArticleService {

    private final InMemoryStorage storage;
    private final ReviewService reviewService;

    public ArticleResponse createArticle(CreateArticleRequest request) {
        Article article = Article.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .category(request.getCategory())
                .authorId(request.getAuthorId())
                .status(ArticleStatus.DRAFT)
                .version(1)
                .score(0.0)
                .viewCount(0)
                .isHot(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        storage.saveArticle(article);
        log.info("Created article: {}", article.getId());
        return toResponse(article);
    }

    public ArticleResponse updateArticle(Long id, UpdateArticleRequest request) {
        Article article = storage.getArticle(id);
        if (article == null) {
            throw new RuntimeException("Article not found");
        }

        if (article.getStatus() == ArticleStatus.PUBLISHED) {
            article.setVersion(article.getVersion() + 1);
            article.setStatus(ArticleStatus.DRAFT);
        }

        article.setTitle(request.getTitle());
        article.setContent(request.getContent());
        article.setCategory(request.getCategory());
        article.setUpdatedAt(LocalDateTime.now());

        storage.saveArticle(article);
        log.info("Updated article: {}", id);
        return toResponse(article);
    }

    public void deleteArticle(Long id) {
        Article article = storage.getArticle(id);
        if (article == null) {
            throw new RuntimeException("Article not found");
        }

        if (storage.existsTicketByArticleId(id)) {
            throw new RuntimeException("Article is referenced by tickets and cannot be deleted");
        }

        storage.deleteArticle(id);
        log.info("Deleted article: {}", id);
    }

    public ArticleResponse getArticle(Long id) {
        Article article = storage.getArticle(id);
        if (article == null) {
            throw new RuntimeException("Article not found");
        }
        return toResponse(article);
    }

    public List<ArticleResponse> getAllArticles() {
        return storage.getAllArticles().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<ArticleResponse> getArticlesByStatus(ArticleStatus status) {
        return storage.getArticlesByStatus(status).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<ArticleResponse> getArticlesByCategory(String category) {
        return storage.getArticlesByCategory(category).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public ArticleResponse submitForReview(Long articleId) {
        Article article = storage.getArticle(articleId);
        if (article == null) {
            throw new RuntimeException("Article not found");
        }

        article.setStatus(ArticleStatus.PENDING_REVIEW);
        article.setUpdatedAt(LocalDateTime.now());

        String reviewerId = reviewService.assignReviewer(article.getCategory());
        article.setReviewerId(reviewerId);

        storage.saveArticle(article);
        log.info("Article {} submitted for review, assigned to reviewer {}", articleId, reviewerId);
        return toResponse(article);
    }

    public ArticleResponse publishArticle(Long articleId, String reviewerId) {
        Article article = storage.getArticle(articleId);
        if (article == null) {
            throw new RuntimeException("Article not found");
        }

        if (article.getStatus() != ArticleStatus.APPROVED) {
            throw new RuntimeException("Article must be approved before publishing");
        }

        ArticleVersion version = ArticleVersion.builder()
                .articleId(articleId)
                .title(article.getTitle())
                .content(article.getContent())
                .version(article.getVersion())
                .publishedBy(reviewerId)
                .publishedAt(LocalDateTime.now())
                .build();
        storage.saveArticleVersion(version);

        article.setStatus(ArticleStatus.PUBLISHED);
        article.setPublishedAt(LocalDateTime.now());
        article.setUpdatedAt(LocalDateTime.now());

        storage.saveArticle(article);
        log.info("Article {} published", articleId);
        return toResponse(article);
    }

    public ArticleResponse rollbackToVersion(Long articleId, Integer version) {
        ArticleVersion versionEntity = storage.getArticleVersion(articleId, version);
        if (versionEntity == null) {
            throw new RuntimeException("Version not found");
        }

        Article article = storage.getArticle(articleId);
        if (article == null) {
            throw new RuntimeException("Article not found");
        }

        article.setTitle(versionEntity.getTitle());
        article.setContent(versionEntity.getContent());
        article.setVersion(article.getVersion() + 1);
        article.setStatus(ArticleStatus.DRAFT);
        article.setUpdatedAt(LocalDateTime.now());

        storage.saveArticle(article);
        log.info("Article {} rolled back to version {}", articleId, version);
        return toResponse(article);
    }

    public List<ArticleVersion> getArticleVersions(Long articleId) {
        return storage.getArticleVersions(articleId);
    }

    private ArticleResponse toResponse(Article article) {
        return ArticleResponse.builder()
                .id(article.getId())
                .title(article.getTitle())
                .content(article.getContent())
                .category(article.getCategory())
                .authorId(article.getAuthorId())
                .reviewerId(article.getReviewerId())
                .status(article.getStatus())
                .version(article.getVersion())
                .score(article.getScore())
                .viewCount(article.getViewCount())
                .isHot(article.getIsHot())
                .referencedByTicket(storage.existsTicketByArticleId(article.getId()))
                .createdAt(article.getCreatedAt())
                .updatedAt(article.getUpdatedAt())
                .publishedAt(article.getPublishedAt())
                .build();
    }
}
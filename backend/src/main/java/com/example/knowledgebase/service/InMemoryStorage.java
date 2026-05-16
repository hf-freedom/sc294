package com.example.knowledgebase.service;

import com.example.knowledgebase.model.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Component
public class InMemoryStorage {

    private final ConcurrentHashMap<Long, Article> articles = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Long, ArticleVersion> articleVersions = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Long, User> users = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Long, Ticket> tickets = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Long, OptimizeTask> optimizeTasks = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Long, ReviewStats> reviewStats = new ConcurrentHashMap<>();

    private final AtomicLong articleIdGenerator = new AtomicLong(1);
    private final AtomicLong versionIdGenerator = new AtomicLong(1);
    private final AtomicLong userIdGenerator = new AtomicLong(1);
    private final AtomicLong ticketIdGenerator = new AtomicLong(1);
    private final AtomicLong taskIdGenerator = new AtomicLong(1);
    private final AtomicLong statsIdGenerator = new AtomicLong(1);

    public Long saveArticle(Article article) {
        if (article.getId() == null) {
            article.setId(articleIdGenerator.getAndIncrement());
        }
        articles.put(article.getId(), article);
        return article.getId();
    }

    public Article getArticle(Long id) {
        return articles.get(id);
    }

    public List<Article> getAllArticles() {
        return new ArrayList<>(articles.values());
    }

    public void deleteArticle(Long id) {
        articles.remove(id);
    }

    public List<Article> getArticlesByStatus(Article.ArticleStatus status) {
        return articles.values().stream()
                .filter(a -> a.getStatus() == status)
                .collect(Collectors.toList());
    }

    public List<Article> getArticlesByCategory(String category) {
        return articles.values().stream()
                .filter(a -> category.equals(a.getCategory()))
                .collect(Collectors.toList());
    }

    public List<Article> getArticlesByReviewerId(String reviewerId) {
        return articles.values().stream()
                .filter(a -> reviewerId.equals(a.getReviewerId()))
                .collect(Collectors.toList());
    }

    public List<Article> getArticlesByScoreLessThan(Double score) {
        return articles.values().stream()
                .filter(a -> a.getScore() != null && a.getScore() < score)
                .collect(Collectors.toList());
    }

    public List<Article> getExpiredArticles(java.time.LocalDateTime dateTime) {
        return articles.values().stream()
                .filter(a -> a.getExpiresAt() != null && a.getExpiresAt().isBefore(dateTime))
                .collect(Collectors.toList());
    }

    public List<Article> getHotArticles() {
        return articles.values().stream()
                .filter(a -> Boolean.TRUE.equals(a.getIsHot()))
                .collect(Collectors.toList());
    }

    public Long saveArticleVersion(ArticleVersion version) {
        if (version.getId() == null) {
            version.setId(versionIdGenerator.getAndIncrement());
        }
        articleVersions.put(version.getId(), version);
        return version.getId();
    }

    public List<ArticleVersion> getArticleVersions(Long articleId) {
        return articleVersions.values().stream()
                .filter(v -> articleId.equals(v.getArticleId()))
                .sorted((a, b) -> b.getVersion().compareTo(a.getVersion()))
                .collect(Collectors.toList());
    }

    public ArticleVersion getArticleVersion(Long articleId, Integer version) {
        return articleVersions.values().stream()
                .filter(v -> articleId.equals(v.getArticleId()) && version.equals(v.getVersion()))
                .findFirst()
                .orElse(null);
    }

    public Long saveUser(User user) {
        if (user.getId() == null) {
            user.setId(userIdGenerator.getAndIncrement());
        }
        users.put(user.getId(), user);
        return user.getId();
    }

    public User getUser(Long id) {
        return users.get(id);
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    public List<User> getUsersByRole(User.UserRole role) {
        return users.values().stream()
                .filter(u -> u.getRole() == role)
                .collect(Collectors.toList());
    }

    public List<User> getUsersByRoleAndCategory(User.UserRole role, String category) {
        return users.values().stream()
                .filter(u -> u.getRole() == role && category.equals(u.getCategory()))
                .collect(Collectors.toList());
    }

    public Long saveTicket(Ticket ticket) {
        if (ticket.getId() == null) {
            ticket.setId(ticketIdGenerator.getAndIncrement());
        }
        tickets.put(ticket.getId(), ticket);
        return ticket.getId();
    }

    public boolean existsTicketByArticleId(Long articleId) {
        return tickets.values().stream()
                .anyMatch(t -> articleId.equals(t.getArticleId()));
    }

    public Long saveOptimizeTask(OptimizeTask task) {
        if (task.getId() == null) {
            task.setId(taskIdGenerator.getAndIncrement());
        }
        optimizeTasks.put(task.getId(), task);
        return task.getId();
    }

    public OptimizeTask getOptimizeTask(Long id) {
        return optimizeTasks.get(id);
    }

    public List<OptimizeTask> getAllOptimizeTasks() {
        return new ArrayList<>(optimizeTasks.values());
    }

    public List<OptimizeTask> getOptimizeTasksByStatus(OptimizeTask.TaskStatus status) {
        return optimizeTasks.values().stream()
                .filter(t -> t.getStatus() == status)
                .collect(Collectors.toList());
    }

    public boolean existsOptimizeTaskByArticleId(Long articleId) {
        return optimizeTasks.values().stream()
                .anyMatch(t -> articleId.equals(t.getArticleId()));
    }

    public void deleteOptimizeTask(Long id) {
        optimizeTasks.remove(id);
    }

    public Long saveReviewStats(ReviewStats stats) {
        if (stats.getId() == null) {
            stats.setId(statsIdGenerator.getAndIncrement());
        }
        reviewStats.put(stats.getId(), stats);
        return stats.getId();
    }

    public ReviewStats getReviewStatsByDate(java.time.LocalDate date) {
        return reviewStats.values().stream()
                .filter(s -> date.equals(s.getDate()))
                .findFirst()
                .orElse(null);
    }

    public ReviewStats getTodayReviewStats() {
        return getReviewStatsByDate(java.time.LocalDate.now());
    }
}
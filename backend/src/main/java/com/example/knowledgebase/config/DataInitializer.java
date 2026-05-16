package com.example.knowledgebase.config;

import com.example.knowledgebase.model.Article;
import com.example.knowledgebase.model.Article.ArticleStatus;
import com.example.knowledgebase.model.User;
import com.example.knowledgebase.model.User.UserRole;
import com.example.knowledgebase.model.Ticket;
import com.example.knowledgebase.service.InMemoryStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final InMemoryStorage storage;

    @Override
    public void run(String... args) {
        initUsers();
        initArticles();
        initTickets();
    }

    private void initUsers() {
        if (storage.getAllUsers().isEmpty()) {
            storage.saveUser(User.builder()
                    .username("author1")
                    .password("password")
                    .email("author1@example.com")
                    .role(UserRole.AUTHOR)
                    .build());

            storage.saveUser(User.builder()
                    .username("reviewer_tech")
                    .password("password")
                    .email("reviewer_tech@example.com")
                    .role(UserRole.REVIEWER)
                    .category("技术")
                    .build());

            storage.saveUser(User.builder()
                    .username("reviewer_product")
                    .password("password")
                    .email("reviewer_product@example.com")
                    .role(UserRole.REVIEWER)
                    .category("产品")
                    .build());

            storage.saveUser(User.builder()
                    .username("senior_reviewer")
                    .password("password")
                    .email("senior@example.com")
                    .role(UserRole.SENIOR_REVIEWER)
                    .build());

            storage.saveUser(User.builder()
                    .username("admin")
                    .password("password")
                    .email("admin@example.com")
                    .role(UserRole.ADMIN)
                    .build());

            log.info("Initialized users");
        }
    }

    private void initArticles() {
        if (storage.getAllArticles().isEmpty()) {
            storage.saveArticle(Article.builder()
                    .title("Spring Boot入门指南")
                    .content("这是一篇关于Spring Boot入门的文章，介绍了如何快速搭建Spring Boot项目。")
                    .category("技术")
                    .authorId("1")
                    .status(ArticleStatus.DRAFT)
                    .version(1)
                    .score(0.0)
                    .viewCount(0)
                    .isHot(false)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build());

            storage.saveArticle(Article.builder()
                    .title("Vue.js最佳实践")
                    .content("本文介绍Vue.js开发中的最佳实践和常见模式。")
                    .category("技术")
                    .authorId("1")
                    .status(ArticleStatus.PENDING_REVIEW)
                    .reviewerId("2")
                    .version(1)
                    .score(0.0)
                    .viewCount(150)
                    .isHot(false)
                    .createdAt(LocalDateTime.now().minusDays(1))
                    .updatedAt(LocalDateTime.now())
                    .build());

            storage.saveArticle(Article.builder()
                    .title("产品设计方法论")
                    .content("介绍产品设计的核心方法论和流程。")
                    .category("产品")
                    .authorId("1")
                    .status(ArticleStatus.APPROVED)
                    .reviewerId("3")
                    .version(2)
                    .score(4.5)
                    .viewCount(500)
                    .isHot(true)
                    .createdAt(LocalDateTime.now().minusDays(3))
                    .updatedAt(LocalDateTime.now())
                    .publishedAt(LocalDateTime.now().minusDays(2))
                    .build());

            storage.saveArticle(Article.builder()
                    .title("低质量文章示例")
                    .content("内容质量较低，需要优化。")
                    .category("技术")
                    .authorId("1")
                    .status(ArticleStatus.PUBLISHED)
                    .reviewerId("2")
                    .version(1)
                    .score(2.5)
                    .viewCount(50)
                    .isHot(false)
                    .createdAt(LocalDateTime.now().minusDays(7))
                    .updatedAt(LocalDateTime.now())
                    .publishedAt(LocalDateTime.now().minusDays(6))
                    .expiresAt(LocalDateTime.now().minusDays(1))
                    .build());

            log.info("Initialized articles");
        }
    }

    private void initTickets() {
        if (storage.getAllOptimizeTasks().isEmpty()) {
            storage.saveTicket(Ticket.builder()
                    .title("问题咨询")
                    .description("用户咨询关于产品设计方法论的问题")
                    .articleId(3L)
                    .creatorId("1")
                    .createdAt(LocalDateTime.now())
                    .build());

            log.info("Initialized tickets");
        }
    }
}
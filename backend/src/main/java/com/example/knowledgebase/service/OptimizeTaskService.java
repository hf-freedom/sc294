package com.example.knowledgebase.service;

import com.example.knowledgebase.model.Article;
import com.example.knowledgebase.model.OptimizeTask;
import com.example.knowledgebase.model.OptimizeTask.TaskStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OptimizeTaskService {

    private final InMemoryStorage storage;

    private static final double LOW_SCORE_THRESHOLD = 3.0;

    public void checkAndCreateOptimizeTasks() {
        List<Article> lowScoreArticles = storage.getArticlesByScoreLessThan(LOW_SCORE_THRESHOLD);

        for (Article article : lowScoreArticles) {
            if (!storage.existsOptimizeTaskByArticleId(article.getId())) {
                OptimizeTask task = OptimizeTask.builder()
                        .articleId(article.getId())
                        .articleTitle(article.getTitle())
                        .currentScore(article.getScore())
                        .reason("文章评分低于阈值 " + LOW_SCORE_THRESHOLD)
                        .status(TaskStatus.PENDING)
                        .createdAt(LocalDateTime.now())
                        .build();

                storage.saveOptimizeTask(task);
                log.info("Created optimize task for article {} with score {}", article.getId(), article.getScore());
            }
        }
    }

    public List<OptimizeTask> getPendingTasks() {
        return storage.getOptimizeTasksByStatus(TaskStatus.PENDING);
    }

    public List<OptimizeTask> getAllTasks() {
        return storage.getAllOptimizeTasks();
    }

    public OptimizeTask completeTask(Long taskId) {
        OptimizeTask task = storage.getOptimizeTask(taskId);
        if (task == null) {
            throw new RuntimeException("Task not found");
        }

        task.setStatus(TaskStatus.COMPLETED);
        task.setCompletedAt(LocalDateTime.now());

        storage.saveOptimizeTask(task);
        log.info("Completed optimize task: {}", taskId);
        return task;
    }
}
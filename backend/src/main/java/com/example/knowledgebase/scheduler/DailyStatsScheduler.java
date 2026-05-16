package com.example.knowledgebase.scheduler;

import com.example.knowledgebase.service.OptimizeTaskService;
import com.example.knowledgebase.service.StatsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DailyStatsScheduler {

    private final StatsService statsService;
    private final OptimizeTaskService optimizeTaskService;

    @Scheduled(cron = "0 0 2 * * ?")
    public void generateDailyStats() {
        log.info("Starting daily stats generation...");
        statsService.generateDailyStats();
        log.info("Daily stats generation completed");
    }

    @Scheduled(cron = "0 0 3 * * ?")
    public void checkLowScoreArticles() {
        log.info("Starting low score article check...");
        optimizeTaskService.checkAndCreateOptimizeTasks();
        log.info("Low score article check completed");
    }

    @Scheduled(cron = "0 * * * * ?")
    public void generateHourlyStats() {
        statsService.generateDailyStats();
        optimizeTaskService.checkAndCreateOptimizeTasks();
    }
}
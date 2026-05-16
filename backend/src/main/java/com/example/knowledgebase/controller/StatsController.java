package com.example.knowledgebase.controller;

import com.example.knowledgebase.dto.response.ApiResponse;
import com.example.knowledgebase.dto.response.ReviewStatsResponse;
import com.example.knowledgebase.service.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;

    @GetMapping("/today")
    public ResponseEntity<ApiResponse<ReviewStatsResponse>> getTodayStats() {
        ReviewStatsResponse stats = statsService.getTodayStats();
        return ResponseEntity.ok(ApiResponse.success(stats));
    }

    @PostMapping("/generate")
    public ResponseEntity<ApiResponse<ReviewStatsResponse>> generateStats() {
        ReviewStatsResponse stats = statsService.generateDailyStats();
        return ResponseEntity.ok(ApiResponse.success("Stats generated successfully", stats));
    }
}
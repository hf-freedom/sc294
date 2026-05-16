package com.example.knowledgebase.controller;

import com.example.knowledgebase.dto.response.ApiResponse;
import com.example.knowledgebase.entity.OptimizeTask;
import com.example.knowledgebase.service.OptimizeTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/optimize-tasks")
@RequiredArgsConstructor
public class OptimizeTaskController {

    private final OptimizeTaskService optimizeTaskService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<OptimizeTask>>> getTasks(
            @RequestParam(required = false) String status) {
        List<OptimizeTask> tasks;
        if ("pending".equalsIgnoreCase(status)) {
            tasks = optimizeTaskService.getPendingTasks();
        } else {
            tasks = optimizeTaskService.getAllTasks();
        }
        return ResponseEntity.ok(ApiResponse.success(tasks));
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<ApiResponse<OptimizeTask>> completeTask(@PathVariable Long id) {
        OptimizeTask task = optimizeTaskService.completeTask(id);
        return ResponseEntity.ok(ApiResponse.success("Task completed", task));
    }

    @PostMapping("/check")
    public ResponseEntity<ApiResponse<Void>> checkLowScoreArticles() {
        optimizeTaskService.checkAndCreateOptimizeTasks();
        return ResponseEntity.ok(ApiResponse.success("Checked low score articles", null));
    }
}
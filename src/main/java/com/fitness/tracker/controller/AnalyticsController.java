package com.fitness.tracker.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fitness.tracker.dto.WeeklyAnalyticsResponse;
import com.fitness.tracker.service.AnalyticsService;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/weekly/{userId}")
    public ResponseEntity<WeeklyAnalyticsResponse> getWeeklyAnalytics(@PathVariable Long userId) {
        return ResponseEntity.ok(analyticsService.getWeeklyAnalytics(userId));
    }
}

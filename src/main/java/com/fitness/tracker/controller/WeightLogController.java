package com.fitness.tracker.controller;

import java.util.List;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fitness.tracker.entity.WeightLog;
import com.fitness.tracker.service.WeightLogService;

@RestController
@RequestMapping("/api/weight-logs")
public class WeightLogController {

    private final WeightLogService weightLogService;

    public WeightLogController(WeightLogService weightLogService) { this.weightLogService = weightLogService; }

    @GetMapping
    public List<WeightLog> getAllWeightLogs() { return weightLogService.getAllWeightLogs(); }
    @GetMapping("/{id}")
    public WeightLog getWeightLogById(@PathVariable Long id) { return weightLogService.getWeightLogById(id); }
    @GetMapping("/user/{userId}")
    public List<WeightLog> getWeightLogsByUserId(@PathVariable Long userId) { return weightLogService.getWeightLogsByUserId(userId); }
    @PostMapping
    public ResponseEntity<WeightLog> createWeightLog(@Valid @RequestBody WeightLog weightLog) {
        return ResponseEntity.status(HttpStatus.CREATED).body(weightLogService.createWeightLog(weightLog));
    }
    @PutMapping("/{id}")
    public WeightLog updateWeightLog(@PathVariable Long id, @Valid @RequestBody WeightLog weightLog) {
        return weightLogService.updateWeightLog(id, weightLog);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWeightLog(@PathVariable Long id) {
        weightLogService.deleteWeightLog(id);
        return ResponseEntity.noContent().build();
    }
}

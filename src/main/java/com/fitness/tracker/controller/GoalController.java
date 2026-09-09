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

import com.fitness.tracker.entity.Goal;
import com.fitness.tracker.service.GoalService;

@RestController
@RequestMapping("/api/goals")
public class GoalController {

    private final GoalService goalService;

    public GoalController(GoalService goalService) { this.goalService = goalService; }

    @GetMapping
    public List<Goal> getAllGoals() { return goalService.getAllGoals(); }
    @GetMapping("/{id}")
    public Goal getGoalById(@PathVariable Long id) { return goalService.getGoalById(id); }
    @GetMapping("/user/{userId}")
    public List<Goal> getGoalsByUserId(@PathVariable Long userId) { return goalService.getGoalsByUserId(userId); }
    @PostMapping
    public ResponseEntity<Goal> createGoal(@Valid @RequestBody Goal goal) {
        return ResponseEntity.status(HttpStatus.CREATED).body(goalService.createGoal(goal));
    }
    @PutMapping("/{id}")
    public Goal updateGoal(@PathVariable Long id, @Valid @RequestBody Goal goal) { return goalService.updateGoal(id, goal); }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGoal(@PathVariable Long id) {
        goalService.deleteGoal(id);
        return ResponseEntity.noContent().build();
    }
}

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

import com.fitness.tracker.entity.Meal;
import com.fitness.tracker.service.MealService;

@RestController
@RequestMapping("/api/meals")
public class MealController {

    private final MealService mealService;

    public MealController(MealService mealService) { this.mealService = mealService; }

    @GetMapping
    public List<Meal> getAllMeals() { return mealService.getAllMeals(); }
    @GetMapping("/{id}")
    public Meal getMealById(@PathVariable Long id) { return mealService.getMealById(id); }
    @GetMapping("/user/{userId}")
    public List<Meal> getMealsByUserId(@PathVariable Long userId) { return mealService.getMealsByUserId(userId); }
    @PostMapping
    public ResponseEntity<Meal> createMeal(@Valid @RequestBody Meal meal) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mealService.createMeal(meal));
    }
    @PutMapping("/{id}")
    public Meal updateMeal(@PathVariable Long id, @Valid @RequestBody Meal meal) { return mealService.updateMeal(id, meal); }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMeal(@PathVariable Long id) {
        mealService.deleteMeal(id);
        return ResponseEntity.noContent().build();
    }
}

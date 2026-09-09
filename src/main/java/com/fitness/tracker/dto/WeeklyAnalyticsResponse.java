package com.fitness.tracker.dto;

import java.time.LocalDate;

public class WeeklyAnalyticsResponse {

    private Long userId;
    private LocalDate weekStart;
    private LocalDate weekEnd;
    private Long totalWorkouts;
    private Integer totalWorkoutDuration;
    private Integer totalCaloriesBurned;
    private Integer totalMealCalories;
    private Double averageProtein;
    private Double averageCarbs;
    private Double averageFat;
    private Double startingWeight;
    private Double latestWeight;
    private Double weightChange;
    private Long totalGoals;
    private Long completedGoals;

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public LocalDate getWeekStart() { return weekStart; }
    public void setWeekStart(LocalDate weekStart) { this.weekStart = weekStart; }
    public LocalDate getWeekEnd() { return weekEnd; }
    public void setWeekEnd(LocalDate weekEnd) { this.weekEnd = weekEnd; }
    public Long getTotalWorkouts() { return totalWorkouts; }
    public void setTotalWorkouts(Long totalWorkouts) { this.totalWorkouts = totalWorkouts; }
    public Integer getTotalWorkoutDuration() { return totalWorkoutDuration; }
    public void setTotalWorkoutDuration(Integer totalWorkoutDuration) { this.totalWorkoutDuration = totalWorkoutDuration; }
    public Integer getTotalCaloriesBurned() { return totalCaloriesBurned; }
    public void setTotalCaloriesBurned(Integer totalCaloriesBurned) { this.totalCaloriesBurned = totalCaloriesBurned; }
    public Integer getTotalMealCalories() { return totalMealCalories; }
    public void setTotalMealCalories(Integer totalMealCalories) { this.totalMealCalories = totalMealCalories; }
    public Double getAverageProtein() { return averageProtein; }
    public void setAverageProtein(Double averageProtein) { this.averageProtein = averageProtein; }
    public Double getAverageCarbs() { return averageCarbs; }
    public void setAverageCarbs(Double averageCarbs) { this.averageCarbs = averageCarbs; }
    public Double getAverageFat() { return averageFat; }
    public void setAverageFat(Double averageFat) { this.averageFat = averageFat; }
    public Double getStartingWeight() { return startingWeight; }
    public void setStartingWeight(Double startingWeight) { this.startingWeight = startingWeight; }
    public Double getLatestWeight() { return latestWeight; }
    public void setLatestWeight(Double latestWeight) { this.latestWeight = latestWeight; }
    public Double getWeightChange() { return weightChange; }
    public void setWeightChange(Double weightChange) { this.weightChange = weightChange; }
    public Long getTotalGoals() { return totalGoals; }
    public void setTotalGoals(Long totalGoals) { this.totalGoals = totalGoals; }
    public Long getCompletedGoals() { return completedGoals; }
    public void setCompletedGoals(Long completedGoals) { this.completedGoals = completedGoals; }
}

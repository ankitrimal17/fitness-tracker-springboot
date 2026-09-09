package com.fitness.tracker.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fitness.tracker.dto.WeeklyAnalyticsResponse;
import com.fitness.tracker.entity.Goal;
import com.fitness.tracker.entity.Meal;
import com.fitness.tracker.entity.WeightLog;
import com.fitness.tracker.entity.Workout;
import com.fitness.tracker.exception.ResourceNotFoundException;
import com.fitness.tracker.repository.GoalRepository;
import com.fitness.tracker.repository.MealRepository;
import com.fitness.tracker.repository.UserRepository;
import com.fitness.tracker.repository.WeightLogRepository;
import com.fitness.tracker.repository.WorkoutRepository;

@Service
public class AnalyticsService {

    private final UserRepository userRepository;
    private final WorkoutRepository workoutRepository;
    private final MealRepository mealRepository;
    private final GoalRepository goalRepository;
    private final WeightLogRepository weightLogRepository;

    public AnalyticsService(UserRepository userRepository, WorkoutRepository workoutRepository,
            MealRepository mealRepository, GoalRepository goalRepository, WeightLogRepository weightLogRepository) {
        this.userRepository = userRepository;
        this.workoutRepository = workoutRepository;
        this.mealRepository = mealRepository;
        this.goalRepository = goalRepository;
        this.weightLogRepository = weightLogRepository;
    }

    public WeeklyAnalyticsResponse getWeeklyAnalytics(Long userId) {
        verifyUserExists(userId);

        LocalDate today = LocalDate.now();
        LocalDate weekStart = today.with(DayOfWeek.MONDAY);
        LocalDate weekEnd = today.with(DayOfWeek.SUNDAY);

        List<Workout> weeklyWorkouts = workoutRepository.findByUserId(userId).stream()
                .filter(workout -> isWithinWeek(workout.getDate(), weekStart, weekEnd))
                .toList();
        List<Meal> weeklyMeals = mealRepository.findByUserId(userId).stream()
                .filter(meal -> isWithinWeek(meal.getDate(), weekStart, weekEnd))
                .toList();
        List<WeightLog> weeklyWeightLogs = weightLogRepository.findByUserId(userId).stream()
                .filter(weightLog -> isWithinWeek(weightLog.getDate(), weekStart, weekEnd))
                .sorted(Comparator.comparing(WeightLog::getDate))
                .toList();
        List<Goal> userGoals = goalRepository.findByUserId(userId);

        double startingWeight = weeklyWeightLogs.stream()
                .findFirst()
                .map(WeightLog::getWeight)
                .orElse(0.0);
        double latestWeight = weeklyWeightLogs.stream()
                .reduce((first, second) -> second)
                .map(WeightLog::getWeight)
                .orElse(0.0);

        WeeklyAnalyticsResponse response = new WeeklyAnalyticsResponse();
        response.setUserId(userId);
        response.setWeekStart(weekStart);
        response.setWeekEnd(weekEnd);
        response.setTotalWorkouts(weeklyWorkouts.stream().count());
        response.setTotalWorkoutDuration(weeklyWorkouts.stream().mapToInt(Workout::getDuration).sum());
        response.setTotalCaloriesBurned(weeklyWorkouts.stream().mapToInt(Workout::getCaloriesBurned).sum());
        response.setTotalMealCalories(weeklyMeals.stream().mapToInt(Meal::getCalories).sum());
        response.setAverageProtein(averageProtein(weeklyMeals));
        response.setAverageCarbs(averageCarbs(weeklyMeals));
        response.setAverageFat(averageFat(weeklyMeals));
        response.setStartingWeight(startingWeight);
        response.setLatestWeight(latestWeight);
        response.setWeightChange(round(latestWeight - startingWeight));
        response.setTotalGoals(userGoals.stream().count());
        response.setCompletedGoals(userGoals.stream()
                .filter(goal -> "completed".equalsIgnoreCase(goal.getStatus()))
                .count());
        return response;
    }

    private void verifyUserExists(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }
    }

    private boolean isWithinWeek(LocalDate date, LocalDate weekStart, LocalDate weekEnd) {
        return !date.isBefore(weekStart) && !date.isAfter(weekEnd);
    }

    private double averageProtein(List<Meal> meals) {
        return round(meals.stream().mapToDouble(Meal::getProtein).average().orElse(0.0));
    }

    private double averageCarbs(List<Meal> meals) {
        return round(meals.stream().mapToDouble(Meal::getCarbs).average().orElse(0.0));
    }

    private double averageFat(List<Meal> meals) {
        return round(meals.stream().mapToDouble(Meal::getFat).average().orElse(0.0));
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}

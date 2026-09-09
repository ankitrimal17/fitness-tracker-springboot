package com.fitness.tracker.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fitness.tracker.entity.Meal;
import com.fitness.tracker.entity.User;
import com.fitness.tracker.exception.ResourceNotFoundException;
import com.fitness.tracker.repository.MealRepository;
import com.fitness.tracker.repository.UserRepository;

@Service
public class MealService {

    private final MealRepository mealRepository;
    private final UserRepository userRepository;

    public MealService(MealRepository mealRepository, UserRepository userRepository) {
        this.mealRepository = mealRepository;
        this.userRepository = userRepository;
    }

    public List<Meal> getAllMeals() { return mealRepository.findAll(); }
    public Meal getMealById(Long id) {
        return mealRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Meal not found with id: " + id));
    }
    public List<Meal> getMealsByUserId(Long userId) { return mealRepository.findByUserId(userId); }
    public Meal createMeal(Meal meal) {
        meal.setUser(getReferencedUser(meal.getUser()));
        return mealRepository.save(meal);
    }
    public Meal updateMeal(Long id, Meal meal) {
        Meal existingMeal = getMealById(id);
        existingMeal.setUser(getReferencedUser(meal.getUser()));
        existingMeal.setDate(meal.getDate());
        existingMeal.setMealType(meal.getMealType());
        existingMeal.setFoodName(meal.getFoodName());
        existingMeal.setCalories(meal.getCalories());
        existingMeal.setProtein(meal.getProtein());
        existingMeal.setCarbs(meal.getCarbs());
        existingMeal.setFat(meal.getFat());
        return mealRepository.save(existingMeal);
    }
    public void deleteMeal(Long id) { mealRepository.delete(getMealById(id)); }
    private User getReferencedUser(User user) {
        if (user == null || user.getId() == null) throw new ResourceNotFoundException("User reference is required");
        return userRepository.findById(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + user.getId()));
    }
}

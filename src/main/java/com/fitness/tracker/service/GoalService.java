package com.fitness.tracker.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fitness.tracker.entity.Goal;
import com.fitness.tracker.entity.User;
import com.fitness.tracker.exception.ResourceNotFoundException;
import com.fitness.tracker.repository.GoalRepository;
import com.fitness.tracker.repository.UserRepository;

@Service
public class GoalService {

    private final GoalRepository goalRepository;
    private final UserRepository userRepository;

    public GoalService(GoalRepository goalRepository, UserRepository userRepository) {
        this.goalRepository = goalRepository;
        this.userRepository = userRepository;
    }

    public List<Goal> getAllGoals() { return goalRepository.findAll(); }
    public Goal getGoalById(Long id) {
        return goalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Goal not found with id: " + id));
    }
    public List<Goal> getGoalsByUserId(Long userId) { return goalRepository.findByUserId(userId); }
    public Goal createGoal(Goal goal) {
        goal.setUser(getReferencedUser(goal.getUser()));
        return goalRepository.save(goal);
    }
    public Goal updateGoal(Long id, Goal goal) {
        Goal existingGoal = getGoalById(id);
        existingGoal.setUser(getReferencedUser(goal.getUser()));
        existingGoal.setGoalType(goal.getGoalType());
        existingGoal.setTargetValue(goal.getTargetValue());
        existingGoal.setCurrentValue(goal.getCurrentValue());
        existingGoal.setStartDate(goal.getStartDate());
        existingGoal.setTargetDate(goal.getTargetDate());
        existingGoal.setStatus(goal.getStatus());
        return goalRepository.save(existingGoal);
    }
    public void deleteGoal(Long id) { goalRepository.delete(getGoalById(id)); }
    private User getReferencedUser(User user) {
        if (user == null || user.getId() == null) throw new ResourceNotFoundException("User reference is required");
        return userRepository.findById(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + user.getId()));
    }
}

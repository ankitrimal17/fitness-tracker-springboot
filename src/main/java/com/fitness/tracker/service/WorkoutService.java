package com.fitness.tracker.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fitness.tracker.entity.User;
import com.fitness.tracker.entity.Workout;
import com.fitness.tracker.exception.ResourceNotFoundException;
import com.fitness.tracker.repository.UserRepository;
import com.fitness.tracker.repository.WorkoutRepository;

@Service
public class WorkoutService {

    private final WorkoutRepository workoutRepository;
    private final UserRepository userRepository;

    public WorkoutService(WorkoutRepository workoutRepository, UserRepository userRepository) {
        this.workoutRepository = workoutRepository;
        this.userRepository = userRepository;
    }

    public List<Workout> getAllWorkouts() { return workoutRepository.findAll(); }

    public Workout getWorkoutById(Long id) {
        return workoutRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Workout not found with id: " + id));
    }

    public List<Workout> getWorkoutsByUserId(Long userId) { return workoutRepository.findByUserId(userId); }

    public Workout createWorkout(Workout workout) {
        workout.setUser(getReferencedUser(workout.getUser()));
        return workoutRepository.save(workout);
    }

    public Workout updateWorkout(Long id, Workout workout) {
        Workout existingWorkout = getWorkoutById(id);
        existingWorkout.setUser(getReferencedUser(workout.getUser()));
        existingWorkout.setDate(workout.getDate());
        existingWorkout.setType(workout.getType());
        existingWorkout.setDuration(workout.getDuration());
        existingWorkout.setCaloriesBurned(workout.getCaloriesBurned());
        existingWorkout.setNotes(workout.getNotes());
        return workoutRepository.save(existingWorkout);
    }

    public void deleteWorkout(Long id) { workoutRepository.delete(getWorkoutById(id)); }

    private User getReferencedUser(User user) {
        if (user == null || user.getId() == null) {
            throw new ResourceNotFoundException("User reference is required");
        }
        return userRepository.findById(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + user.getId()));
    }
}

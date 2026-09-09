package com.fitness.tracker.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fitness.tracker.entity.User;
import com.fitness.tracker.entity.WeightLog;
import com.fitness.tracker.exception.ResourceNotFoundException;
import com.fitness.tracker.repository.UserRepository;
import com.fitness.tracker.repository.WeightLogRepository;

@Service
public class WeightLogService {

    private final WeightLogRepository weightLogRepository;
    private final UserRepository userRepository;

    public WeightLogService(WeightLogRepository weightLogRepository, UserRepository userRepository) {
        this.weightLogRepository = weightLogRepository;
        this.userRepository = userRepository;
    }

    public List<WeightLog> getAllWeightLogs() { return weightLogRepository.findAll(); }
    public WeightLog getWeightLogById(Long id) {
        return weightLogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Weight log not found with id: " + id));
    }
    public List<WeightLog> getWeightLogsByUserId(Long userId) { return weightLogRepository.findByUserId(userId); }
    public WeightLog createWeightLog(WeightLog weightLog) {
        weightLog.setUser(getReferencedUser(weightLog.getUser()));
        return weightLogRepository.save(weightLog);
    }
    public WeightLog updateWeightLog(Long id, WeightLog weightLog) {
        WeightLog existingWeightLog = getWeightLogById(id);
        existingWeightLog.setUser(getReferencedUser(weightLog.getUser()));
        existingWeightLog.setDate(weightLog.getDate());
        existingWeightLog.setWeight(weightLog.getWeight());
        return weightLogRepository.save(existingWeightLog);
    }
    public void deleteWeightLog(Long id) { weightLogRepository.delete(getWeightLogById(id)); }
    private User getReferencedUser(User user) {
        if (user == null || user.getId() == null) throw new ResourceNotFoundException("User reference is required");
        return userRepository.findById(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + user.getId()));
    }
}

package com.crud.api.service.strategy;

import com.crud.api.dto.UserGoalsResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CalculatorService {

    private final Map<String, GoalStrategy> goalStrategy;

    public UserGoalsResponseDTO calculate(Long userId, String goal) {
        GoalStrategy goalStrategy = this.goalStrategy.get(goal);
        if (goalStrategy != null) {
            return goalStrategy.calculate(userId);
        } else {
            throw new IllegalArgumentException("Goal type not supported");
        }
    }
}

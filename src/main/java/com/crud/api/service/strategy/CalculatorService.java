package com.crud.api.service.strategy;

import com.crud.api.dto.UserGoalsResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CalculatorService {

    private final Map<String, GoalStrategy> goalStrategyMap;

    public UserGoalsResponseDTO calculate(Long userId, String goalName) {
        String calculatorType;
        switch (goalName) {
            case "ENERGY_SURPLUS" -> calculatorType = "surplusCalculator";
            case "ENERGY_DEFICIT" -> calculatorType = "deficitCalculator";
            case "ENERGY_TDEE" -> calculatorType = "tdeeCalculator";
            default ->  throw new IllegalArgumentException("Goal type not supported");
        }
        GoalStrategy goalStrategy = this.goalStrategyMap.get(calculatorType);
        return goalStrategy.calculate(userId);
    }
}

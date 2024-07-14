package com.crud.api.service.strategy;

import com.crud.api.dto.UserGoalsResponseDTO;

public interface GoalStrategy {

    UserGoalsResponseDTO calculate(long userId);
}

package com.crud.api.service.strategy;

import com.crud.api.dto.UserGoalsResponseDTO;
import com.crud.api.entity.Measurement;
import com.crud.api.entity.User;
import com.crud.api.mapper.ResponseMeasurementMapper;
import com.crud.api.repository.MeasurementRepository;
import com.crud.api.repository.UserRepository;
import com.crud.api.service.NutritionService;
import com.crud.api.service.base.AbstractGoalCalculator;
import org.springframework.stereotype.Component;

@Component
public class TdeeCalculator extends AbstractGoalCalculator implements GoalStrategy {

    public TdeeCalculator(UserRepository userRepository, MeasurementRepository measurementRepository, NutritionService nutritionService, ResponseMeasurementMapper responseMeasurementMapper) {
        super(userRepository, measurementRepository, nutritionService, responseMeasurementMapper);
    }

    @Override
    public UserGoalsResponseDTO calculate(long userId) {

        /* fetch User from database */
        User user = fetchUser(userId);
        Measurement currentWeight = fetchUserCurrentWeight(userId);

        /* calculate BMR */
        double bmr = calculateBasalMetabolicRate(user);

        /* calculate tdee */
        double tdee = calculateTotalDailyEnergyExpenditure(bmr, user.getActivity());

        /* store User's TDEE in database */
        Measurement tdeeEntity = createTdeeEntity(tdee);

        return getUserGoalsResponseDTO(user, currentWeight, tdeeEntity);
    }
}

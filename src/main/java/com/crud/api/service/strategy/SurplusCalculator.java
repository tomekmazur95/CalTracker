package com.crud.api.service.strategy;

import com.crud.api.dto.UserGoalsResponseDTO;
import com.crud.api.entity.Measurement;
import com.crud.api.entity.User;
import com.crud.api.enums.MeasureType;
import com.crud.api.enums.Unit;
import com.crud.api.mapper.ResponseMeasurementMapper;
import com.crud.api.repository.MeasurementRepository;
import com.crud.api.repository.UserRepository;
import com.crud.api.service.NutritionService;
import com.crud.api.service.base.AbstractGoalCalculator;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

import static com.crud.api.util.GoalUtils.SURPLUS;

@Component("ENERGY_SURPLUS")
public class SurplusCalculator extends AbstractGoalCalculator implements GoalStrategy {

    public SurplusCalculator(UserRepository userRepository, MeasurementRepository measurementRepository, NutritionService nutritionService, ResponseMeasurementMapper responseMeasurementMapper) {
        super(userRepository, measurementRepository, nutritionService, responseMeasurementMapper);
    }

    @Override
    public UserGoalsResponseDTO calculate(long userId) {
        /* fetch User from database  */
        User user = fetchUser(userId);
        Measurement currentWeight = fetchUserCurrentWeight(userId);

        /* fetch tdee */
        double tdee = resolveLastUserTdee(user);

        /* calculate surplus */
        double surplus = tdee + tdee * SURPLUS;

        /* store user's surplus in database */
        Measurement surplusEntity = createSurplusEntity(surplus);

        return getUserGoalsResponseDTO(user, currentWeight, surplusEntity);
    }

    private Measurement createSurplusEntity(double surplus) {
        Measurement surplusEntity = new Measurement();
        surplusEntity.setType(MeasureType.ENERGY_SURPLUS);
        surplusEntity.setUnit(Unit.CALORIES);
        surplusEntity.setValue((double) Math.round(surplus));
        surplusEntity.setDate(LocalDate.now());
        return surplusEntity;
    }
}

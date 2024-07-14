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

import static com.crud.api.util.GoalUtils.DEFICIT;

@Component("ENERGY_DEFICIT")
public class DeficitCalculator extends AbstractGoalCalculator implements GoalStrategy {

    public DeficitCalculator(UserRepository userRepository, MeasurementRepository measurementRepository, NutritionService nutritionService, ResponseMeasurementMapper responseMeasurementMapper) {
        super(userRepository, measurementRepository, nutritionService, responseMeasurementMapper);
    }

    @Override
    public UserGoalsResponseDTO calculate(long userId) {

        /*  fetch User from database  */
        User user = fetchUser(userId);
        Measurement currentWeight = fetchUserCurrentWeight(userId);

        /* fetch tdee */
        double tdee = resolveLastUserTdee(user);

        /* calculate deficit */
        double deficit = tdee - tdee * DEFICIT;

        /* store user's deficit in database */
        Measurement deficitEntity = createDeficitEntity(deficit);

        return getUserGoalsResponseDTO(user, currentWeight, deficitEntity);
    }

    private Measurement createDeficitEntity(double deficit) {
        Measurement deficitEntity = new Measurement();
        deficitEntity.setType(MeasureType.ENERGY_DEFICIT);
        deficitEntity.setUnit(Unit.CALORIES);
        deficitEntity.setValue((double) Math.round(deficit));
        deficitEntity.setDate(LocalDate.now());
        return deficitEntity;
    }
}

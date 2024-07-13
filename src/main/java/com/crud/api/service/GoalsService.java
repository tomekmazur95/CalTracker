package com.crud.api.service;

import com.crud.api.dto.ResponseMeasurementDTO;
import com.crud.api.dto.ResponseNutritionDTO;
import com.crud.api.dto.UserGoalsResponseDTO;
import com.crud.api.entity.Measurement;
import com.crud.api.entity.Nutrition;
import com.crud.api.entity.User;
import com.crud.api.enums.Activity;
import com.crud.api.enums.MeasureType;
import com.crud.api.error.MeasurementNotFoundException;
import com.crud.api.error.NutritionNotFoundException;
import com.crud.api.error.UserNotFoundException;
import com.crud.api.mapper.ResponseMeasurementMapper;
import com.crud.api.mapper.ResponseNutritionMapper;
import com.crud.api.repository.MeasurementRepository;
import com.crud.api.repository.NutritionRepository;
import com.crud.api.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

import static com.crud.api.util.ConstantsUtils.*;

@Service
@RequiredArgsConstructor
public class GoalsService {

    private final UserRepository userRepository;
    private final MeasurementRepository measurementRepository;
    private final ResponseMeasurementMapper responseMeasurementMapper;
    private final ResponseNutritionMapper responseNutritionMapper;
    private final NutritionRepository nutritionRepository;

    @Transactional
    public UserGoalsResponseDTO findUserGoals(Long userId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(String.format(USER_NOT_FOUND, userId)));
        Activity activity = user.getActivity();

        Measurement currentWeight = fetchLatestCurrentWeight(userId);
        Measurement goal = fetchLatestGoal(userId);
        Nutrition nutrition = fetchLatestNutrition(goal.getId());

        ResponseMeasurementDTO weightResponseDTO = responseMeasurementMapper.fromDomain(currentWeight);
        ResponseMeasurementDTO goalResponseDTO = responseMeasurementMapper.fromDomain(goal);
        ResponseNutritionDTO nutritionResponseDTO = responseNutritionMapper.fromDomain(nutrition);
        return UserGoalsResponseDTO.builder()
            .activity(activity)
            .currentWeight(weightResponseDTO)
            .goal(goalResponseDTO)
            .nutrition(nutritionResponseDTO)
            .build();
    }

    private Measurement fetchLatestCurrentWeight(Long userId) {
        List<Measurement> list = measurementRepository.findAllByUserId(userId)
            .stream()
            .filter(e -> e.getType().equals(MeasureType.CURRENT_WEIGHT))
            .sorted(Comparator.comparing(Measurement::getId).reversed())
            .toList();

        if (list.isEmpty()) {
            throw new MeasurementNotFoundException(String.format(MEASUREMENT_TYPE_NOT_FOUND, MeasureType.CURRENT_WEIGHT, userId));
        }
        return list.get(0);
    }

    private Measurement fetchLatestGoal(Long userId) {
        List<Measurement> listOfGoals = measurementRepository.findAllByUserId(userId)
            .stream()
            .filter(e -> e.getType().equals(MeasureType.ENERGY_TDEE)
                || e.getType().equals(MeasureType.ENERGY_DEFICIT)
                || e.getType().equals(MeasureType.ENERGY_SURPLUS))
            .sorted(Comparator.comparing(Measurement::getId).reversed())
            .toList();
        if (listOfGoals.isEmpty()) {
            throw new MeasurementNotFoundException(String.format(MEASUREMENT_TYPE_NOT_FOUND, ENERGY_GOAL, userId));
        }
        return listOfGoals.get(0);
    }

    private Nutrition fetchLatestNutrition(Long goalId) {
        List<Nutrition> listOfNutritions = nutritionRepository.findAllByMeasurementId(goalId)
                .stream()
                .sorted(Comparator.comparing(Nutrition::getId).reversed())
                .toList();
        if (listOfNutritions.isEmpty()) {
            throw new NutritionNotFoundException(String.format(NUTRITION_NOT_FOUND, goalId));
        }
        return listOfNutritions.get(0);
    }
}

package com.crud.api.service;

import com.crud.api.dto.RequestNutritionDTO;
import com.crud.api.dto.ResponseNutritionDTO;
import com.crud.api.entity.Measurement;
import com.crud.api.entity.Nutrition;
import com.crud.api.error.NutritionNotFoundException;
import com.crud.api.mapper.RequestNutritionMapper;
import com.crud.api.mapper.ResponseNutritionMapper;
import com.crud.api.repository.NutritionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import static com.crud.api.util.ConstantsUtils.NUTRITION_ID_NOT_FOUND;
import static com.crud.api.util.NutritionUtils.*;

@Service
@RequiredArgsConstructor
public class NutritionService {

    private final NutritionRepository nutritionRepository;
    private final ResponseNutritionMapper responseNutritionMapper;
    private final RequestNutritionMapper requestNutritionMapper;

    public ResponseNutritionDTO calculateDefaultNutritions(Measurement goal) {
        Map<String, Long> nutritionsMap = calculateNutritions(goal, CARBS_DEFAULT_PERCENTAGE, FAT_DEFAULT_PERCENTAGE, PROTEIN_DEFAULT_PERCENTAGE);
        Nutrition domain = requestNutritionMapper.toDomain(nutritionsMap);
        domain.setMeasurement(goal);
        domain = nutritionRepository.save(domain);
        return responseNutritionMapper.fromDomain(domain);
    }

    @Transactional
    public ResponseNutritionDTO updateNutritions(RequestNutritionDTO requestNutritionDTO, Long nutritionId) {
        Nutrition domain = nutritionRepository.findById(nutritionId)
                .orElseThrow(() -> new NutritionNotFoundException(String.format(NUTRITION_ID_NOT_FOUND, nutritionId)));
        Measurement goal = domain.getMeasurement();
        Map<String, Long> nutritionsMap = calculateNutritions(goal, requestNutritionDTO.getCarbs(), requestNutritionDTO.getFat(), requestNutritionDTO.getProtein());
        requestNutritionMapper.editableToDomain(requestNutritionDTO, domain, nutritionsMap);
        return responseNutritionMapper.fromDomain(domain);
    }

    private Map<String, Long> calculateNutritions(Measurement goal, double carbsPercentage, double fatPercentage, double proteinPercentage) {
        Map<String, Long> nutritionsMap = new HashMap<>();
        Double calories = goal.getValue();
        nutritionsMap.put("carbs", singleNutritionCalculate(calories, carbsPercentage, CARBS_PER_GRAM));
        nutritionsMap.put("fat", singleNutritionCalculate(calories, fatPercentage, FAT_PER_GRAM));
        nutritionsMap.put("protein", singleNutritionCalculate(calories, proteinPercentage, PROTEIN_PER_GRAM));
        return nutritionsMap;
    }

    private long singleNutritionCalculate(Double calories, double nutritionPercentage, double caloriePerGram) {
        return Math.round(calories * nutritionPercentage / caloriePerGram);
    }
}

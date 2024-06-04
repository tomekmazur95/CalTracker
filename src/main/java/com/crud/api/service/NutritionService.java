package com.crud.api.service;

import com.crud.api.dto.RequestNutritionDTO;
import com.crud.api.dto.ResponseNutritionDTO;
import com.crud.api.entity.Measurement;
import com.crud.api.entity.Nutrition;
import com.crud.api.enums.MacroElement;
import com.crud.api.error.MeasurementNotFoundException;
import com.crud.api.error.NutritionNotFoundException;
import com.crud.api.mapper.RequestNutritionMapper;
import com.crud.api.mapper.ResponseNutritionMapper;
import com.crud.api.repository.MeasurementRepository;
import com.crud.api.repository.NutritionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NutritionService {


    private static final String NUTRITION_NOT_FOUND = "Nutrition with id: %s not found";
    private static final String MEASUREMENT_NOT_FOUND = "Measurement with id: %s not found";
    public static final double CARBS_DEFAULT_PERCENTAGE = MacroElement.CARBOHYDRATE.getDefaultPercentage();
    public static final double FAT_DEFAULT_PERCENTAGE = MacroElement.FAT.getDefaultPercentage();
    public static final double PROTEIN_DEFAULT_PERCENTAGE = MacroElement.PROTEIN.getDefaultPercentage();
    public static final double CARBS_PER_GRAM = MacroElement.CARBOHYDRATE.getCaloriePerGram();
    public static final double FAT_PER_GRAM = MacroElement.FAT.getCaloriePerGram();
    public static final double PROTEIN_PER_GRAM = MacroElement.PROTEIN.getCaloriePerGram();

    private final NutritionRepository nutritionRepository;
    private final ResponseNutritionMapper responseNutritionMapper;
    private final RequestNutritionMapper requestNutritionMapper;
    private final MeasurementRepository measurementRepository;

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
                .orElseThrow(() -> new NutritionNotFoundException(String.format(NUTRITION_NOT_FOUND, nutritionId)));
        Long goalID = domain.getMeasurement().getId();
        Measurement goal = measurementRepository.findById(goalID)
                .orElseThrow(() -> new MeasurementNotFoundException(String.format(MEASUREMENT_NOT_FOUND, goalID)));

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

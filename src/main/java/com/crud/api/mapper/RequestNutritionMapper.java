package com.crud.api.mapper;

import com.crud.api.dto.RequestNutritionDTO;
import com.crud.api.entity.Nutrition;
import com.crud.api.enums.MacroElement;
import com.crud.api.enums.Unit;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Map;

@Component
public class RequestNutritionMapper {

    public Nutrition toDomain(Map<String, Long> nutritionsMap) {
        Nutrition domain = emptyDomain();
        domain.setCarbohydrate(nutritionsMap.get("carbs"));
        domain.setFat(nutritionsMap.get("fat"));
        domain.setProtein(nutritionsMap.get("protein"));
        domain.setCarbohydratePercent(MacroElement.CARBOHYDRATE.getDefaultPercentage());
        domain.setFatPercent(MacroElement.FAT.getDefaultPercentage());
        domain.setProteinPercent(MacroElement.PROTEIN.getDefaultPercentage());
        domain.setDate(LocalDate.now());
        domain.setUnit(Unit.GRAMS);
        return domain;
    }

    public void editableToDomain(RequestNutritionDTO dto, Nutrition domain, Map<String, Long> nutritionsMap) {
        domain.setCarbohydratePercent(dto.getCarbs());
        domain.setFatPercent(dto.getFat());
        domain.setProteinPercent(dto.getProtein());
        domain.setCarbohydrate(nutritionsMap.get("carbs"));
        domain.setFat(nutritionsMap.get("fat"));
        domain.setProtein(nutritionsMap.get("protein"));
        domain.setDate(LocalDate.now());
    }

    private Nutrition emptyDomain() {
        return new Nutrition();
    }
}

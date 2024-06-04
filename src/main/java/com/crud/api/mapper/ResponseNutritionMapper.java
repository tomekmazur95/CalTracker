package com.crud.api.mapper;

import com.crud.api.dto.ResponseNutritionDTO;
import com.crud.api.entity.Nutrition;
import org.springframework.stereotype.Component;

@Component
public class ResponseNutritionMapper {

    public ResponseNutritionDTO fromDomain(Nutrition domain) {
        ResponseNutritionDTO dto = createEmptyDto();
        dto.setId(domain.getId());
        dto.setFatPercent(domain.getFatPercent());
        dto.setCarbohydratePercent(domain.getCarbohydratePercent());
        dto.setProteinPercent(domain.getProteinPercent());
        dto.setFat(domain.getFat());
        dto.setCarbohydrate(domain.getCarbohydrate());
        dto.setProtein(domain.getProtein());
        dto.setUnit(domain.getUnit());
        dto.setDate(domain.getDate());
        return dto;
    }

    private ResponseNutritionDTO createEmptyDto() {
        return new ResponseNutritionDTO();
    }

}

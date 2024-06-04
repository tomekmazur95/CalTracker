package com.crud.api.mapper;

import com.crud.api.dto.ResponseFoodFactDTO;
import com.crud.api.entity.FoodFact;
import org.springframework.stereotype.Component;

@Component
public class ResponseFoodFactMapper {

    public ResponseFoodFactDTO fromDomain(FoodFact domain) {
        ResponseFoodFactDTO dto = createEmptyDTO();
        dto.setId(domain.getId());
        dto.setUnit(domain.getUnit());
        dto.setValue(domain.getValue());
        dto.setCalories(domain.getCalories());
        dto.setCarbohydrate(domain.getCarbohydrate());
        dto.setProtein(domain.getProtein());
        dto.setFat(domain.getFat());
        dto.setDate(domain.getDate());
        return dto;
    }

    private ResponseFoodFactDTO createEmptyDTO() {
        return new ResponseFoodFactDTO();
    }
}

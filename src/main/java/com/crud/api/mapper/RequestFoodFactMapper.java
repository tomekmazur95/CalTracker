package com.crud.api.mapper;

import com.crud.api.dto.RequestFoodFactDTO;
import com.crud.api.entity.FoodFact;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class RequestFoodFactMapper {

    public FoodFact toDomain(RequestFoodFactDTO dto) {
        FoodFact domain = emptyDomain();
        domain.setUnit(dto.getUnit());
        domain.setValue(dto.getValue());
        domain.setCalories(dto.getCalories());
        domain.setFat(dto.getFat());
        domain.setCarbohydrate(dto.getCarbohydrate());
        domain.setProtein(dto.getProtein());
        domain.setDate(dto.getDate() == null ? LocalDate.now() : dto.getDate());
        return domain;
    }

    private FoodFact emptyDomain() {
        return new FoodFact();
    }

}

package com.crud.api.mapper;

import com.crud.api.entity.Food;
import com.crud.api.entity.MealFood;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class MealFoodMapper {

    public MealFood toDomain(Map.Entry<Food, Double> entry) {
        MealFood domain = emptyDomain();
        domain.setFood(entry.getKey());
        domain.setFoodQuantity(entry.getValue());
        return domain;
    }

    private MealFood emptyDomain() {
        return new MealFood();
    }
}


package com.crud.api.mapper;

import com.crud.api.dto.ResponseFoodDTO;
import com.crud.api.dto.ResponseMealDTO;
import com.crud.api.entity.Food;
import com.crud.api.entity.Meal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ResponseMealMapper {


    private final ResponseFoodMapper responseFoodMapper;

    public ResponseMealDTO fromDomain(Meal domain, Map<Food, Double> mealFoodMap) {
        ResponseMealDTO dto = createEmptyDTO();
        dto.setMealId(domain.getId());
        dto.setName(domain.getName());
        dto.setDescription(domain.getDescription());
        dto.setDate(domain.getDate());
        dto.setResponseFoodDtoMap(createResponseFoodDtoMap(mealFoodMap));
        return dto;
    }

    private ResponseMealDTO createEmptyDTO() {
        return new ResponseMealDTO();
    }

    private Map<ResponseFoodDTO, Double> createResponseFoodDtoMap(Map<Food, Double> mealFoodMap) {
        return mealFoodMap.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        e -> responseFoodMapper.fromDomain(e.getKey()),
                        Map.Entry::getValue)
                );
    }
}

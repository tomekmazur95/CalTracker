package com.crud.api.mapper;

import com.crud.api.dto.ResponseFoodDTO;
import com.crud.api.entity.Food;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ResponseFoodMapper {

    private final ResponseFoodFactMapper responseFoodFactMapper;

    public ResponseFoodDTO fromDomain(Food foodDomain) {
        ResponseFoodDTO dto = createEmptyDTO();
        dto.setId(foodDomain.getId());
        dto.setDate(foodDomain.getDate());
        dto.setName(foodDomain.getName());
        dto.setDescription(foodDomain.getDescription());
        dto.setResponseFoodFactDTO(responseFoodFactMapper.fromDomain(foodDomain.getFoodFact()));
        return dto;
    }

    private ResponseFoodDTO createEmptyDTO() {
        return new ResponseFoodDTO();
    }
}

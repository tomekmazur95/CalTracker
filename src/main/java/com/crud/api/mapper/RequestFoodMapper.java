package com.crud.api.mapper;

import com.crud.api.dto.RequestFoodDTO;
import com.crud.api.entity.Food;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class RequestFoodMapper {

    private final RequestFoodFactMapper requestFoodFactMapper;

    public Food toDomain(RequestFoodDTO dto) {
        Food domain = emptyDomain();
        domain.setName(dto.getName());
        domain.setDescription(dto.getDescription());
        domain.setDate(dto.getDate() == null ? LocalDate.now() : dto.getDate());
        domain.setFoodFact(requestFoodFactMapper.toDomain(dto.getRequestFoodFactDTO()));
        return domain;
    }


    private Food emptyDomain() {
        return new Food();
    }
}

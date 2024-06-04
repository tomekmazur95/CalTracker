package com.crud.api.mapper;

import com.crud.api.dto.RequestMealDTO;
import com.crud.api.entity.Meal;
import com.crud.api.entity.User;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class RequestMealMapper {


    public Meal toDomain(RequestMealDTO dto, User user) {
        Meal domain = emptyDomain();
        domain.setUser(user);
        domain.setName(dto.getMealName());
        domain.setDate(dto.getDate() != null ? dto.getDate() : LocalDate.now());
        domain.setDescription(dto.getDescription());
        return domain;
    }

    private Meal emptyDomain() {
        return new Meal();
    }
}

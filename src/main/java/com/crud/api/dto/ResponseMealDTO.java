package com.crud.api.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseMealDTO implements Serializable {

    private Long mealId;
    private String name;
    private String description;
    private LocalDate date;
    private Map<ResponseFoodDTO, Double> responseFoodDtoMap;
}

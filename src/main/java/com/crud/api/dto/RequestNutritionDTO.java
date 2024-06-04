package com.crud.api.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestNutritionDTO implements Serializable {

    private Double carbs;
    private Double protein;
    private Double fat;
}

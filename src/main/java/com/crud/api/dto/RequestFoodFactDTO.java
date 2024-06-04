package com.crud.api.dto;

import com.crud.api.enums.Unit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestFoodFactDTO implements Serializable {

    private Unit unit;
    private Double value;
    private Double calories;
    private Double fat;
    private Double carbohydrate;
    private Double protein;
    private LocalDate date;
}

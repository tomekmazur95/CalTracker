package com.crud.api.dto;

import com.crud.api.enums.Unit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseFoodFactDTO implements Serializable {

    private Long id;
    private Unit unit;
    private Double value;
    private Double calories;
    private Double fat;
    private Double carbohydrate;
    private Double protein;
    private LocalDate date;
}

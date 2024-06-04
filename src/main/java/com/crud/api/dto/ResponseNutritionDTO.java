package com.crud.api.dto;

import com.crud.api.enums.Unit;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseNutritionDTO implements Serializable {

    private Long id;
    private Double fatPercent;
    private Double carbohydratePercent;
    private Double proteinPercent;
    private Long fat;
    private Long carbohydrate;
    private Long protein;
    @Enumerated(EnumType.STRING)
    private Unit unit;
    private LocalDate date;
}

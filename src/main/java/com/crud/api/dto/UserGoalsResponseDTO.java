package com.crud.api.dto;


import com.crud.api.enums.Activity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UserGoalsResponseDTO implements Serializable {

    private ResponseMeasurementDTO goal;
    private ResponseMeasurementDTO currentWeight;
    private Activity activity;
    private ResponseNutritionDTO nutrition;

}

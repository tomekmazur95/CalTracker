package com.crud.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestMealDTO implements Serializable {
    String mealName;
    String description;
    LocalDate date;
    List<RequestFoodQtyDTO> foodList;
}

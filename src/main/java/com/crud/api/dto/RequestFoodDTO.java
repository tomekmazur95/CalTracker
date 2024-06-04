package com.crud.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestFoodDTO implements Serializable {

    private String name;
    private String description;
    private LocalDate date;
    private RequestFoodFactDTO requestFoodFactDTO;
}

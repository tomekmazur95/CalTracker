package com.crud.api.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseFoodDTO implements Serializable {

    private Long id;
    private String name;
    private String description;
    private LocalDate date;
    private ResponseFoodFactDTO responseFoodFactDTO;
}

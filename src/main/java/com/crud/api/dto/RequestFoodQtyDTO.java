package com.crud.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestFoodQtyDTO implements Serializable {

    private Long foodId;
    private Double quantity;

}

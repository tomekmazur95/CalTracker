package com.crud.api.dto;

import com.crud.api.enums.MeasureType;
import com.crud.api.enums.Unit;
import java.io.Serializable;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestMeasurementDTO implements Serializable {

    private MeasureType type;
    private Double value;
    private Unit unit;
    private LocalDate date;
}

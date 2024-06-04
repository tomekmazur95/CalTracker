package com.crud.api.dto;

import com.crud.api.enums.MeasureType;
import com.crud.api.enums.Unit;
import java.io.Serializable;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseMeasurementDTO implements Serializable {
    private Long id;
    private MeasureType type;
    private Double value;
    private Unit unit;
    private LocalDate date;
}


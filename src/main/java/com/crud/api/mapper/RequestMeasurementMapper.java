package com.crud.api.mapper;

import com.crud.api.dto.RequestMeasurementDTO;
import com.crud.api.entity.Measurement;
import java.time.LocalDate;
import org.springframework.stereotype.Component;

@Component
public class RequestMeasurementMapper {

    public Measurement toDomain(RequestMeasurementDTO dto) {
        Measurement domain = emptyDomain();
        domain.setType(dto.getType());
        domain.setValue(dto.getValue());
        domain.setUnit(dto.getUnit());
        domain.setDate(dto.getDate() == null ? LocalDate.now() : dto.getDate());
        return domain;
    }

    public Measurement editableToDomain(RequestMeasurementDTO dto, Measurement domain) {
        domain.setType(dto.getType());
        domain.setValue(dto.getValue());
        domain.setUnit(dto.getUnit());
        domain.setDate(dto.getDate() == null ? LocalDate.now() : dto.getDate());
        return domain;
    }


    private Measurement emptyDomain() {
        return new Measurement();
    }

}

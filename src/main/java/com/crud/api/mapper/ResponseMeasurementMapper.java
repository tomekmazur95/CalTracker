package com.crud.api.mapper;

import com.crud.api.dto.ResponseMeasurementDTO;
import com.crud.api.entity.Measurement;
import org.springframework.stereotype.Component;

@Component
public class ResponseMeasurementMapper {

    public ResponseMeasurementDTO fromDomain(Measurement domain) {
        ResponseMeasurementDTO dto = createEmptyDTO();
        dto.setId(domain.getId());
        dto.setType(domain.getType());
        dto.setValue(domain.getValue());
        dto.setUnit(domain.getUnit());
        dto.setDate(domain.getDate());
        return dto;
    }

    private ResponseMeasurementDTO createEmptyDTO() {
        return new ResponseMeasurementDTO();
    }
}

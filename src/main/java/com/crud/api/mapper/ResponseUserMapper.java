package com.crud.api.mapper;

import com.crud.api.dto.ResponseUserDTO;
import com.crud.api.entity.Measurement;
import com.crud.api.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ResponseUserMapper {

    private final ResponseMeasurementMapper responseMeasurementMapper;

    public ResponseUserDTO fromDomain(User domain, Measurement measurementDomain) {
        ResponseUserDTO dto = createEmptyDTO();
        dto.setId(domain.getId());
        dto.setUserName(domain.getUserName());
        dto.setAge(domain.getAge());
        dto.setGender(domain.getGender());
        dto.setActivity(domain.getActivity());
        dto.setHeight(responseMeasurementMapper.fromDomain(measurementDomain));
        return dto;
    }

    private ResponseUserDTO createEmptyDTO() {
        return new ResponseUserDTO();
    }
}

package com.crud.api.mapper;

import com.crud.api.dto.ResponseUserActivityDTO;
import com.crud.api.entity.User;
import java.io.Serializable;
import org.springframework.stereotype.Component;

@Component
public class ResponseUserActivityMapper implements Serializable {

    public ResponseUserActivityDTO fromDomain(User domain) {
        ResponseUserActivityDTO dto = createEmptyDto();
        dto.setId(domain.getId());
        dto.setActivity(domain.getActivity());
        return dto;
    }

    private ResponseUserActivityDTO createEmptyDto() {
        return new ResponseUserActivityDTO();
    }

}

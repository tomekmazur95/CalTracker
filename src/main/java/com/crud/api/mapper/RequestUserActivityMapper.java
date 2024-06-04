package com.crud.api.mapper;

import com.crud.api.dto.RequestUserActivityDTO;
import com.crud.api.entity.User;
import org.springframework.stereotype.Component;

@Component
public class RequestUserActivityMapper {
    public void fillInDomain(RequestUserActivityDTO dto, User domain) {
        domain.setActivity(dto.getActivity());
    }

}

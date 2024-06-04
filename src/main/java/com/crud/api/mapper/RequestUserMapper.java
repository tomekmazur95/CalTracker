package com.crud.api.mapper;

import com.crud.api.dto.RequestUserDTO;
import com.crud.api.entity.User;
import org.springframework.stereotype.Component;

@Component
public class RequestUserMapper {

    public User toDomain(RequestUserDTO dto) {
        User domain = emptyDomain();
        domain.setUserName(dto.getUserName());
        domain.setAge(dto.getAge());
        domain.setGender(dto.getGender());
        domain.setActivity(dto.getActivity());
        return domain;
    }

    public User editableToDomain(RequestUserDTO dto, User domain) {
        domain.setUserName(dto.getUserName());
        domain.setAge(dto.getAge());
        domain.setGender(dto.getGender());
        domain.setActivity(dto.getActivity());
        return domain;
    }

    private User emptyDomain() {
        return new User();
    }
}

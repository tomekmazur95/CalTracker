package com.crud.api.service;

import com.crud.api.dto.UserInfoResponse;
import com.crud.api.entity.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserInfoService {
    public UserInfoResponse findUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserInfo principal) {
            return UserInfoResponse.builder()
                    .id(principal.getId())
                    .email(principal.getEmail())
                    .build();
        }
        return null;
    }
}

package com.crud.api.controller;

import com.crud.api.dto.UserInfoResponse;
import com.crud.api.service.UserInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/userInfo")
@RequiredArgsConstructor
public class UserInfoController {

    private final UserInfoService userInfoService;

    @GetMapping()
    public ResponseEntity<UserInfoResponse> findByUser() {
        return ResponseEntity.ok(userInfoService.findUser());
    }
}

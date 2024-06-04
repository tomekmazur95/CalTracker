package com.crud.api.controller;

import com.crud.api.dto.UserGoalsResponseDTO;
import com.crud.api.service.CaloriesCalculatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/{userId}/calories")
@RequiredArgsConstructor
public class CaloriesCalculatorController {

    private final CaloriesCalculatorService caloriesCalculatorService;

    @PostMapping()
    @PreAuthorize(value = "hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<UserGoalsResponseDTO> calculate(@PathVariable Long userId, @RequestParam String goal) {
        return new ResponseEntity<>(caloriesCalculatorService.calculate(userId, goal), HttpStatus.OK);
    }

}

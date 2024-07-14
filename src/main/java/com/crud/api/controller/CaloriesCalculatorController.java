package com.crud.api.controller;

import com.crud.api.dto.UserGoalsResponseDTO;
import com.crud.api.service.strategy.CalculatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/{userId}/calories")
@RequiredArgsConstructor
public class CaloriesCalculatorController {

    private final CalculatorService calculatorService;

    @PostMapping()
    @PreAuthorize(value = "hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<UserGoalsResponseDTO> calculate(@PathVariable Long userId, @RequestParam String goal) {
        return new ResponseEntity<>(calculatorService.calculate(userId, goal), HttpStatus.OK);
    }

}

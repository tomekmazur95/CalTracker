package com.crud.api.controller;

import com.crud.api.dto.UserGoalsResponseDTO;
import com.crud.api.service.GoalsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/goals")
@RequiredArgsConstructor
public class GoalsController {

    private final GoalsService goalsService;

    @GetMapping("/{userId}")
    @PreAuthorize(value = "hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<UserGoalsResponseDTO> findUserGoals(@PathVariable Long userId) {
        return new ResponseEntity<>(goalsService.findUserGoals(userId), HttpStatus.OK);
    }

}

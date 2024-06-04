package com.crud.api.controller;

import com.crud.api.dto.RequestMealDTO;
import com.crud.api.dto.ResponseMealDTO;
import com.crud.api.service.MealService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/meals")
public class MealController {

    private final MealService mealService;

    @PostMapping()
    @PreAuthorize(value = "hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<ResponseMealDTO> createMeal(@RequestBody RequestMealDTO foodList, @RequestParam Long userId) {
        return new ResponseEntity<>(mealService.createMeal(foodList, userId), HttpStatus.CREATED);
    }

    @GetMapping()
    @PreAuthorize(value = "hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<List<ResponseMealDTO>> findAllUserMeals(@RequestParam Long userId) {
        return new ResponseEntity<>(mealService.findAllUserMeals(userId), HttpStatus.OK);
    }
}
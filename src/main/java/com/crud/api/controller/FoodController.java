package com.crud.api.controller;

import com.crud.api.dto.RequestFoodDTO;
import com.crud.api.dto.ResponseFoodDTO;
import com.crud.api.service.FoodService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FoodController {

    private final FoodService foodService;

    @PostMapping("/foods")
    public ResponseEntity<ResponseFoodDTO> createFood(@RequestBody RequestFoodDTO dto, @RequestParam("userId") Long userId) {
        return new ResponseEntity<>(foodService.createFood(dto, userId), HttpStatus.OK);
    }

    @GetMapping("/foods")
    public ResponseEntity<List<ResponseFoodDTO>> findUserFoods(@RequestParam Long userId) {
        return new ResponseEntity<>(foodService.findUserFoods(userId), HttpStatus.OK);
    }
}

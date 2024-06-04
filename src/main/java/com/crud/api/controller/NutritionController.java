package com.crud.api.controller;

import com.crud.api.dto.RequestNutritionDTO;
import com.crud.api.dto.ResponseNutritionDTO;
import com.crud.api.service.NutritionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/nutritions")
public class NutritionController {

    private final NutritionService nutritionService;

    @PutMapping("/{nutritionId}")
    @PreAuthorize(value = "hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<ResponseNutritionDTO> updateNutritions(RequestNutritionDTO requestNutritionDTO, @PathVariable Long nutritionId) {
        return new ResponseEntity<>(nutritionService.updateNutritions(requestNutritionDTO, nutritionId), HttpStatus.OK);
    }
}

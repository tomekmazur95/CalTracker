package com.crud.api.controller.swagger;

import com.crud.api.dto.RequestNutritionDTO;
import com.crud.api.dto.ResponseNutritionDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;

public interface NutritionControllerSwagger {

    @Operation(
            summary = "Update Nutritions",
            description = "Update details of an existing existing nutritions based on the provided ID and nutritions information"
    )
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Nutritions updated successfully, returns updated nutritions details"),
            @ApiResponse(responseCode = "400", description = "Bad request, invalid input data"),
            @ApiResponse(responseCode = "401", description = "In case of unauthorized access"),
            @ApiResponse(responseCode = "404", description = "Nutritions not found for the given ID"),
            @ApiResponse(responseCode = "500", description = "In case of any exception")
    })
    ResponseEntity<ResponseNutritionDTO> updateNutritions(RequestNutritionDTO requestNutritionDTO, Long nutritionId);
}

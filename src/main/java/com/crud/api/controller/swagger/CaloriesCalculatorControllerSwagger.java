package com.crud.api.controller.swagger;

import com.crud.api.dto.UserGoalsResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;

public interface CaloriesCalculatorControllerSwagger {

    @Operation(
            summary = "Calculates daily calorie requirement",
            description = "Calculate daily calorie requirement for specified User id and provided Goal"
    )
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation successful, return User's daily calorie requirement"),
            @ApiResponse(responseCode = "400", description = "Bad request, invalid input"),
            @ApiResponse(responseCode = "401", description = "In case of unauthorized access"),
            @ApiResponse(responseCode = "404", description = "In case of User or Measurement not found"),
            @ApiResponse(responseCode = "500", description = "In case of any exception")
    })
    ResponseEntity<UserGoalsResponseDTO> calculate(Long userId, String goal);
}

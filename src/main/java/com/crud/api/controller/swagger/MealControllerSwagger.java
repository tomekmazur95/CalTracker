package com.crud.api.controller.swagger;

import com.crud.api.dto.RequestMealDTO;
import com.crud.api.dto.ResponseMealDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface MealControllerSwagger {
    @Operation(
            summary = "Create Meal",
            description = "Create meal in the system for specified User"
    )
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Operation successful, meal created"),
            @ApiResponse(responseCode = "400", description = "Bad request, invalid input"),
            @ApiResponse(responseCode = "401", description = "In case of unauthorized access"),
            @ApiResponse(responseCode = "404", description = "In case of User or Food not found"),
            @ApiResponse(responseCode = "500", description = "In case of any exception")
    })
    ResponseEntity<ResponseMealDTO> createMeal(RequestMealDTO foodList, Long userId);

    @Operation(
            summary = "Retrieve All meals for a User",
            description = "Fetches all meals associated with the specified User ID"
    )
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation successful, returns list of meals"),
            @ApiResponse(responseCode = "400", description = "Bad request, invalid input"),
            @ApiResponse(responseCode = "401", description = "In case of unauthorized access"),
            @ApiResponse(responseCode = "404", description = "In case of User or Food not found"),
            @ApiResponse(responseCode = "500", description = "In case of any exception")
    })
    ResponseEntity<List<ResponseMealDTO>> findAllUserMeals(Long userId);
}

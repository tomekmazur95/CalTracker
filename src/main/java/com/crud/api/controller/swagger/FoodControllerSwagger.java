package com.crud.api.controller.swagger;

import com.crud.api.dto.RequestFoodDTO;
import com.crud.api.dto.ResponseFoodDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface FoodControllerSwagger {

    @Operation(
            summary = "Create Food",
            description = "Create food in the system for specified User"
    )
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation successful, food created"),
            @ApiResponse(responseCode = "400", description = "Bad request, invalid input"),
            @ApiResponse(responseCode = "401", description = "In case of unauthorized access"),
            @ApiResponse(responseCode = "500", description = "In case of any exception")
    })
    ResponseEntity<ResponseFoodDTO> createFood(RequestFoodDTO dto, Long userId);

    @Operation(
            summary = "Retrieve All foods for a User",
            description = "Fetches all foods associated with the specified User ID"
    )
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation successful, returns last measurement"),
            @ApiResponse(responseCode = "401", description = "In case of unauthorized access"),
            @ApiResponse(responseCode = "404", description = "User not found for the given ID"),
            @ApiResponse(responseCode = "500", description = "In case of any exception")
    })
    ResponseEntity<List<ResponseFoodDTO>> findUserFoods(Long userId);
}

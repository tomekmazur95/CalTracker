package com.crud.api.controller.swagger;

import com.crud.api.dto.UserGoalsResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;

public interface GoalControllerSwagger {

    @Operation(
            summary = "Retrieve Goal for a User",
            description = "Fetches goal for a specified User ID"
    )
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation successful, returns User's goal"),
            @ApiResponse(responseCode = "400", description = "Bad request, invalid input"),
            @ApiResponse(responseCode = "401", description = "In case of unauthorized access"),
            @ApiResponse(responseCode = "401", description = "Goal not found"),
            @ApiResponse(responseCode = "500", description = "In case of any exception")
    })
    ResponseEntity<UserGoalsResponseDTO> findUserGoals(Long userId);
}

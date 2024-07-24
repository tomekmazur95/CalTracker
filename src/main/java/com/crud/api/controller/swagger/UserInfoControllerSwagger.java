package com.crud.api.controller.swagger;

import com.crud.api.dto.UserInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;

public interface UserInfoControllerSwagger {

    @Operation(
            summary = "Find User information",
            description = "Returns the authenticated User's information based on the current security context.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation, returns user details"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Authentication token not found")
    })
    ResponseEntity<UserInfoResponse> findByUser();
}

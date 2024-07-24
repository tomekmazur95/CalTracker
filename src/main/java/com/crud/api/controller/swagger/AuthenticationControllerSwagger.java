package com.crud.api.controller.swagger;

import com.crud.api.dto.AuthenticationRequest;
import com.crud.api.dto.AuthenticationResponse;
import com.crud.api.dto.RegisterRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;

public interface AuthenticationControllerSwagger {
    @Operation(
            summary = "Register User",
            description = "Register a new User in the system and return JWT token"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation successful, user created"),
            @ApiResponse(responseCode = "409", description = "In case if User already exists"),
            @ApiResponse(responseCode = "500", description = "In case of any exception")
    })
    ResponseEntity<AuthenticationResponse> register(RegisterRequest request);

    @Operation(
            summary = "Authenticate User",
            description = "Authenticate an existing User and return JWT token"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation successful, user authenticated"),
            @ApiResponse(responseCode = "400", description = "Bad request, invalid input data"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "In case of any exception")
    })
    ResponseEntity<AuthenticationResponse> authenticate(AuthenticationRequest request);

}

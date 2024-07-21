package com.crud.api.controller.swagger;

import com.crud.api.dto.RequestUserActivityDTO;
import com.crud.api.dto.RequestUserDTO;
import com.crud.api.dto.ResponseUserActivityDTO;
import com.crud.api.dto.ResponseUserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserControllerSwagger {

    @Operation(
            summary = "Create User",
            description = "Create a new User in the system"
    )
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation successful, user created"),
            @ApiResponse(responseCode = "401", description = "In case of unauthorized access"),
            @ApiResponse(responseCode = "500", description = "In case of any exception")
    })
    ResponseEntity<ResponseUserDTO> createUser(RequestUserDTO dto, Long userInfoId);

    @Operation(
            summary = "Find All Users",
            description = "Retrieve a list of all registered users in the system"
    )
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation successful, returns list of users"),
            @ApiResponse(responseCode = "401", description = "In case of unauthorized access"),
            @ApiResponse(responseCode = "500", description = "In case of any exception")
    })
    ResponseEntity<List<ResponseUserDTO>> findAll();

    @Operation(
            summary = "Find User By Id",
            description = "Retrieve a user by their unique identifier (ID)"
    )
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation successful, returns user details"),
            @ApiResponse(responseCode = "401", description = "In case of unauthorized access"),
            @ApiResponse(responseCode = "404", description = "User not found for the provided ID"),
            @ApiResponse(responseCode = "500", description = "In case of any exception")
    })
    ResponseEntity<ResponseUserDTO> findById(Long userInfoId);


    @Operation(
            summary = "Find User By UserInfo ID",
            description = "Retrieve a user based on the provided userInfo ID"
    )
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found and returned successfully"),
            @ApiResponse(responseCode = "401", description = "In case of unauthorized access"),
            @ApiResponse(responseCode = "404", description = "User not found for the given ID"),
            @ApiResponse(responseCode = "500", description = "In case of any exception")
    })
    ResponseEntity<ResponseUserDTO> findByUserInfoId(Long id);


    @Operation(
            summary = "Update User",
            description = "Update the details of an existing user based on the provided ID and user information"
    )
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully, returns updated user details"),
            @ApiResponse(responseCode = "400", description = "Bad request, invalid input data"),
            @ApiResponse(responseCode = "401", description = "In case of unauthorized access"),
            @ApiResponse(responseCode = "404", description = "User not found for the given ID"),
            @ApiResponse(responseCode = "500", description = "In case of any exception")
    })
    ResponseEntity<ResponseUserDTO> updateUser(Long id, RequestUserDTO dto);


    @Operation(
            summary = "Update User Activity",
            description = "Update the activity details for a user based on the provided ID and activity information"
    )
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Activity updated successfully, returns updated activity details"),
            @ApiResponse(responseCode = "400", description = "Bad request, invalid input data"),
            @ApiResponse(responseCode = "401", description = "In case of unauthorized access"),
            @ApiResponse(responseCode = "404", description = "User not found for the given ID"),
            @ApiResponse(responseCode = "500", description = "In case of any exception")
    })
    ResponseEntity<ResponseUserActivityDTO> updateUserActivity(Long id, RequestUserActivityDTO dto);


    @Operation(
            summary = "Delete User By Id",
            description = "Delete a user from the system based on the provided user ID")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully deleted"),
            @ApiResponse(responseCode = "401", description = "In case of unauthorized access"),
            @ApiResponse(responseCode = "404", description = "User not found for the given ID"),
            @ApiResponse(responseCode = "500", description = "In case of any exception")
    })
    ResponseEntity<Void> deleteUserById(Long id);

}

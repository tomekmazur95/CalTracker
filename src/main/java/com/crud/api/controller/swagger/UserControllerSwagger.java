package com.crud.api.controller.swagger;

import com.crud.api.dto.RequestUserDTO;
import com.crud.api.dto.ResponseUserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.List;
import org.springframework.http.ResponseEntity;

public interface UserControllerSwagger {

    @Operation(summary = "Create User", description = "Create User")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success"),
        @ApiResponse(responseCode = "401", description = "In case of unauthorized access"),
        @ApiResponse(responseCode = "500", description = "In case of any exception")})
    ResponseEntity<ResponseUserDTO> createUser(RequestUserDTO dto, Long userInfoId);

    @Operation(summary = "Find All Users", description = "Find All Users")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success"),
        @ApiResponse(responseCode = "401", description = "In case of unauthorized access"),
        @ApiResponse(responseCode = "500", description = "In case of any exception")})
    ResponseEntity<List<ResponseUserDTO>> findAll();

    @Operation(summary = "Find User By Id", description = "Find User By id")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success"),
        @ApiResponse(responseCode = "401", description = "In case of unauthorized access"),
        @ApiResponse(responseCode = "500", description = "In case of any exception")})
    ResponseEntity<ResponseUserDTO> findById(Long id);

    @Operation(summary = "Update User", description = "Update User")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success"),
        @ApiResponse(responseCode = "401", description = "In case of unauthorized access"),
        @ApiResponse(responseCode = "500", description = "In case of any exception")})
    ResponseEntity<ResponseUserDTO> updateUser(Long id, RequestUserDTO dto);

    @Operation(summary = "Delete User By Id", description = "Delete User By Id")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success"),
        @ApiResponse(responseCode = "401", description = "In case of unauthorized access"),
        @ApiResponse(responseCode = "500", description = "In case of any exception")})
    ResponseEntity<Void> deleteUserById(Long id);

}

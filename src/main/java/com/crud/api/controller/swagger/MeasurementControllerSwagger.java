package com.crud.api.controller.swagger;

import com.crud.api.dto.RequestMeasurementDTO;
import com.crud.api.dto.ResponseMeasurementDTO;
import com.crud.api.enums.MeasureType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface MeasurementControllerSwagger {

    @Operation(
            summary = "Create Measurement",
            description = "Creates a new measurement in the system for a specified user"
    )
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation successful, measurement created"),
            @ApiResponse(responseCode = "400", description = "Bad request, invalid input"),
            @ApiResponse(responseCode = "401", description = "In case of unauthorized access"),
            @ApiResponse(responseCode = "500", description = "In case of any exception")
    })
    ResponseEntity<ResponseMeasurementDTO> createMeasurement(RequestMeasurementDTO dto, Long userId);

    @Operation(
            summary = "Retrieve All Measurements for a User",
            description = "Fetches all measurements associated with the specified user ID"
    )
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation successful, returns list of measurements for specified user"),
            @ApiResponse(responseCode = "400", description = "Bad request, invalid input"),
            @ApiResponse(responseCode = "401", description = "In case of unauthorized access"),
            @ApiResponse(responseCode = "404", description = "User not found for the provided ID"),
            @ApiResponse(responseCode = "500", description = "In case of any exception")
    })
    ResponseEntity<List<ResponseMeasurementDTO>> findAllUserMeasurements(Long userId);


    @Operation(
            summary = "Retrieve the Last Measurement for a User",
            description = "Fetches the last measurement for a specified user ID and measurement type"
    )
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation successful, returns last measurement"),
            @ApiResponse(responseCode = "400", description = "Bad request, invalid input"),
            @ApiResponse(responseCode = "401", description = "In case of unauthorized access"),
            @ApiResponse(responseCode = "404", description = "Measurement not found"),
            @ApiResponse(responseCode = "500", description = "In case of any exception")
    })
    ResponseEntity<ResponseMeasurementDTO> findUserLastMeasurement(Long userId, MeasureType measureType);

}

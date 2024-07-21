package com.crud.api.controller;

import com.crud.api.controller.swagger.MeasurementControllerSwagger;
import com.crud.api.dto.RequestMeasurementDTO;
import com.crud.api.dto.ResponseMeasurementDTO;
import com.crud.api.enums.MeasureType;
import com.crud.api.service.MeasurementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/user/{userId}/measurements")
@RequiredArgsConstructor
public class MeasurementController implements MeasurementControllerSwagger {

    private final MeasurementService measurementService;

    @Override
    @PostMapping()
    @PreAuthorize(value = "hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<ResponseMeasurementDTO> createMeasurement(@RequestBody RequestMeasurementDTO dto, @PathVariable Long userId) {
        return new ResponseEntity<>(measurementService.createMeasurement(userId, dto), HttpStatus.CREATED);
    }

    @Override
    @GetMapping()
    @PreAuthorize(value = "hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<List<ResponseMeasurementDTO>> findAllUserMeasurements(@PathVariable Long userId) {
        return new ResponseEntity<>(measurementService.findAllUserMeasurements(userId), HttpStatus.OK);
    }

    @Override
    @GetMapping("/last")
    @PreAuthorize(value = "hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<ResponseMeasurementDTO> findUserLastMeasurement(@PathVariable Long userId, @RequestParam MeasureType measureType) {
        return new ResponseEntity<>(measurementService.findUserLastMeasurement(userId, measureType), HttpStatus.OK);
    }
}
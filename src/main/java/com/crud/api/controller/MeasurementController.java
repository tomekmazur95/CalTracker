package com.crud.api.controller;

import com.crud.api.dto.RequestMeasurementDTO;
import com.crud.api.dto.ResponseMeasurementDTO;
import com.crud.api.enums.MeasureType;
import com.crud.api.service.MeasurementService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/user/{userId}/measurements")
@RequiredArgsConstructor
public class MeasurementController {

    private final MeasurementService measurementService;

    @PostMapping()
    @PreAuthorize(value = "hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<ResponseMeasurementDTO> createMeasurement(@RequestBody RequestMeasurementDTO dto, @PathVariable Long userId) {
        return new ResponseEntity<>(measurementService.createMeasurement(userId, dto), HttpStatus.CREATED);
    }

    @GetMapping()
    @PreAuthorize(value = "hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<List<ResponseMeasurementDTO>> findAllUserMeasurements(@PathVariable Long userId) {
        return new ResponseEntity<>(measurementService.findAllUserMeasurements(userId), HttpStatus.OK);
    }

    @GetMapping("/last")
    @PreAuthorize(value = "hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<ResponseMeasurementDTO> findUserLastMeasurement(@PathVariable Long userId, @RequestParam MeasureType measureType) {
        return new ResponseEntity<>(measurementService.findUserLastMeasurement(userId, measureType), HttpStatus.OK);
    }
}
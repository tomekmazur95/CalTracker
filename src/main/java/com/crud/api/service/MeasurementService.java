package com.crud.api.service;

import com.crud.api.dto.RequestMeasurementDTO;
import com.crud.api.dto.ResponseMeasurementDTO;
import com.crud.api.entity.Measurement;
import com.crud.api.entity.User;
import com.crud.api.enums.MeasureType;
import com.crud.api.error.MeasurementNotFoundException;
import com.crud.api.error.UserNotFoundException;
import com.crud.api.mapper.RequestMeasurementMapper;
import com.crud.api.mapper.ResponseMeasurementMapper;
import com.crud.api.repository.MeasurementRepository;
import com.crud.api.repository.UserRepository;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MeasurementService {

    private static final String USER_NOT_FOUND = "User with id: %s not found";
    private static final String ID = "id";
    private static final String MEASUREMENT_NOT_FOUND = "Measurement Type: %s not found for User with id %s";

    private final RequestMeasurementMapper requestMeasurementMapper;
    private final ResponseMeasurementMapper responseMeasurementMapper;
    private final UserRepository userRepository;
    private final MeasurementRepository measurementRepository;

    public ResponseMeasurementDTO createMeasurement(Long userId, RequestMeasurementDTO dto) {
        User user = fetchUserByUserId(userId);
        Measurement domain = requestMeasurementMapper.toDomain(dto);
        domain.setUser(user);
        measurementRepository.save(domain);
        return responseMeasurementMapper.fromDomain(domain);
    }

    private User fetchUserByUserId(Long userId) {
         return userRepository.findById(userId)
             .orElseThrow(() -> new UserNotFoundException(String.format(USER_NOT_FOUND, userId)));
    }

    public List<ResponseMeasurementDTO> findAllUserMeasurements(Long userId) {
        List<Measurement> userMeasurementsList = measurementRepository.findAllByUserId(userId, Sort.by(ID));
        return userMeasurementsList.stream()
            .map(responseMeasurementMapper::fromDomain)
            .toList();
    }

    public ResponseMeasurementDTO findUserLastMeasurement(Long userId, MeasureType measureType) {
        List<ResponseMeasurementDTO> list = measurementRepository.findAllByUserId(userId)
            .stream()
            .filter(measure -> measure.getType().equals(measureType))
            .sorted(Comparator.comparing(Measurement::getId).reversed())
            .map(responseMeasurementMapper::fromDomain).toList();

        if (list.isEmpty()) {
            throw new MeasurementNotFoundException(String.format(MEASUREMENT_NOT_FOUND, measureType,userId));
        }
        return list.get(0);
    }
}

package com.crud.api.service;

import com.crud.api.dto.RequestUserActivityDTO;
import com.crud.api.dto.RequestUserDTO;
import com.crud.api.dto.ResponseUserActivityDTO;
import com.crud.api.dto.ResponseUserDTO;
import com.crud.api.entity.Measurement;
import com.crud.api.entity.User;
import com.crud.api.entity.UserInfo;
import com.crud.api.enums.MeasureType;
import com.crud.api.error.UserAlreadyExistsException;
import com.crud.api.error.UserNotFoundException;
import com.crud.api.mapper.RequestMeasurementMapper;
import com.crud.api.mapper.RequestUserActivityMapper;
import com.crud.api.mapper.RequestUserMapper;
import com.crud.api.mapper.ResponseUserActivityMapper;
import com.crud.api.mapper.ResponseUserMapper;
import com.crud.api.repository.MeasurementRepository;
import com.crud.api.repository.UserInfoRepository;
import com.crud.api.repository.UserRepository;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.crud.api.util.ConstantsUtils.*;

@RequiredArgsConstructor
@Service
public class UserService {

    private static final String DATE = "Date";

    private final UserRepository userRepository;
    private final RequestUserMapper requestUserMapper;
    private final ResponseUserMapper responseUserMapper;
    private final RequestMeasurementMapper requestMeasurementMapper;
    private final MeasurementRepository measurementRepository;
    private final UserInfoRepository userInfoRepository;
    private final RequestUserActivityMapper requestUserActivityMapper;
    private final ResponseUserActivityMapper responseUserActivityMapper;

    @Transactional
    public ResponseUserDTO createUser(RequestUserDTO dto, Long userInfoId) {
        if (dto == null || dto.getUserName() == null) {
            throw new IllegalArgumentException("The given fields must not be null");
        }
        if (userRepository.existsByUserInfoId(userInfoId)) {
            throw new UserAlreadyExistsException(String.format(USER_INFO_ALREADY_EXISTS, userInfoId));
        }

        UserInfo userInfo = userInfoRepository.findById(userInfoId)
                .orElseThrow(() -> new UserNotFoundException(String.format(USER_NOT_FOUND, userInfoId)));
        User domain = requestUserMapper.toDomain(dto);
        domain.setUserInfo(userInfo);
        User domainUser = userRepository.save(domain);

        Measurement measureValueDomain = requestMeasurementMapper.toDomain(dto.getHeight());
        measureValueDomain.setUser(domainUser);
        measurementRepository.save(measureValueDomain);

        return responseUserMapper.fromDomain(domain, measureValueDomain);
    }

    @Transactional
    public List<ResponseUserDTO> findAll() {
        return userRepository.findAll()
                .stream()
                .map(domain -> responseUserMapper.fromDomain(domain, fetchHeightByUserId(domain.getId())))
                .toList();
    }

    @Transactional
    public ResponseUserDTO findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(String.format(USER_NOT_FOUND, id)));
        Measurement measurement = fetchHeightByUserId(id);
        return responseUserMapper.fromDomain(user, measurement);
    }

    @Transactional
    public ResponseUserDTO updateUser(Long id, RequestUserDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(String.format(USER_NOT_FOUND, id)));
        user = requestUserMapper.editableToDomain(dto, user);
        userRepository.save(user);

        Measurement height = fetchHeightByUserId(id);
        height = requestMeasurementMapper.editableToDomain(dto.getHeight(), height);
        Measurement measurementDomain = measurementRepository.save(height);

        return responseUserMapper.fromDomain(user, measurementDomain);
    }

    @Transactional
    public void deleteUserById(Long id) {
        if (!userRepository.existsById(id) || !measurementRepository.existsByUserId(id)) {
            throw new UserNotFoundException(String.format(USER_NOT_FOUND, id));
        }
        measurementRepository.deleteByUserId(id);
        userRepository.deleteById(id);
    }

    private Measurement fetchHeightByUserId(Long id) {
        List<Measurement> list = measurementRepository.findAllByUserId(id, Sort.by(DATE));
        List<Measurement> latestHeight = list.stream()
                .filter(e -> e.getType().equals(MeasureType.HEIGHT))
                .sorted(Comparator.comparing(Measurement::getId).reversed())
                .toList();
        return latestHeight.get(0);
    }

    public ResponseUserDTO findByUserInfoId(Long userInfoId) {
        User user = userRepository.findUserByUserInfoId(userInfoId)
                .orElseThrow(() -> new UserNotFoundException(String.format(USER_INFO_ID_NOT_FOUND, userInfoId)));
        Measurement measurement = fetchHeightByUserId(user.getId());
        return responseUserMapper.fromDomain(user, measurement);
    }

    public ResponseUserActivityDTO updateUserActivity(Long id, RequestUserActivityDTO requestUserActivityDTO) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException(String.format(USER_NOT_FOUND, id)));

        requestUserActivityMapper.fillInDomain(requestUserActivityDTO, user);
        userRepository.save(user);

        return responseUserActivityMapper.fromDomain(user);
    }
}

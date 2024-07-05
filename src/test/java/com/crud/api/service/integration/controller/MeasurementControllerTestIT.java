package com.crud.api.service.integration.controller;


import com.crud.api.dto.RequestMeasurementDTO;
import com.crud.api.entity.Measurement;
import com.crud.api.entity.User;
import com.crud.api.entity.UserInfo;
import com.crud.api.enums.Activity;
import com.crud.api.enums.Gender;
import com.crud.api.enums.MeasureType;
import com.crud.api.enums.Unit;
import com.crud.api.repository.MeasurementRepository;
import com.crud.api.repository.UserInfoRepository;
import com.crud.api.repository.UserRepository;
import com.crud.api.service.integration.AppMySQLContainer;
import com.crud.api.service.integration.DatabaseSetupExtension;
import com.crud.api.service.integration.helper.TestEntityFactory;
import com.crud.api.service.integration.helper.TestJsonMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Objects;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(DatabaseSetupExtension.class)
public class MeasurementControllerTestIT extends AppMySQLContainer {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MeasurementRepository measurementRepository;

    @AfterEach
    public void clean() {
        measurementRepository.deleteAll();
        userRepository.deleteAll();
        userInfoRepository.deleteAll();
    }


    @Test
    void shouldThrowForbiddenExceptionWhenUserIsUnauthorized() throws Exception {
        long userId = 1;
        MvcResult result = mockMvc.perform(post("/user/{userId}/measurements", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestJsonMapper.asJsonString(new RequestMeasurementDTO())))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(status().isForbidden())
                .andReturn();

        int status = result.getResponse().getStatus();
        Assertions.assertEquals(403, status);
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    void shouldThrowUserNotFoundExceptionWhenCreateMeasurement() throws Exception {
        long userId = 1;
        MvcResult result = mockMvc.perform(post("/user/{userId}/measurements", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestJsonMapper.asJsonString(new RequestMeasurementDTO())))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(status().isNotFound())
                .andReturn();

        int status = result.getResponse().getStatus();
        String errorMessage = Objects.requireNonNull(result.getResolvedException()).getMessage();
        Assertions.assertEquals(String.format("User with id: %s not found", userId), errorMessage);
        Assertions.assertTrue(userRepository.findById(userId).isEmpty());
        Assertions.assertEquals(404, status);
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    void shouldCreateMeasurement() throws Exception {
        UserInfo userInfoDomain = TestEntityFactory.createUserInfoDomain("john@gmail.com", "password123");
        userInfoRepository.save(userInfoDomain);
        User userDomain = TestEntityFactory.createUserDomain("John", Gender.MALE, Activity.EXTRA_ACTIVE, 53);
        userDomain.setUserInfo(userInfoDomain);
        userRepository.save(userDomain);

        long userId = userDomain.getId();
        RequestMeasurementDTO requestMeasurementDTO = TestEntityFactory.createRequestMeasurementDTO(MeasureType.HEIGHT, Unit.CENTIMETERS, 183.0);

        mockMvc.perform(post("/user/{userId}/measurements", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestJsonMapper.asJsonString(requestMeasurementDTO)))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(status().isCreated());

        Assertions.assertTrue(measurementRepository.existsByUserId(userId));
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    void shouldThrowExceptionWhenUserNotExists() throws Exception {
        long userId = 7L;
        RequestMeasurementDTO requestMeasurementDTO = TestEntityFactory.createRequestMeasurementDTO(MeasureType.HEIGHT, Unit.CENTIMETERS, 183.0);

        MvcResult result = mockMvc.perform(post("/user/{userId}/measurements", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestJsonMapper.asJsonString(requestMeasurementDTO)))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(status().isNotFound())
                .andReturn();

        String errorMessage = Objects.requireNonNull(result.getResolvedException()).getMessage();
        Assertions.assertEquals(String.format("User with id: %s not found", userId), errorMessage);
        Assertions.assertTrue(userRepository.findById(userId).isEmpty());
        Assertions.assertFalse(measurementRepository.existsByUserId(userId));
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    void shouldReturnEmptyMeasurementList() throws Exception {
        UserInfo userInfoDomain = TestEntityFactory.createUserInfoDomain("john@gmail.com", "password123");
        userInfoRepository.save(userInfoDomain);
        User userDomain = TestEntityFactory.createUserDomain("John", Gender.MALE, Activity.EXTRA_ACTIVE, 53);
        userDomain.setUserInfo(userInfoDomain);
        userRepository.save(userDomain);

        long userId = userDomain.getId();

        mockMvc.perform(get("/user/{userId}/measurements", userId))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));


        Assertions.assertTrue(measurementRepository.findAllByUserId(userId).isEmpty());
    }


    @Test
    @WithMockUser(authorities = {"USER"})
    void shouldReturnMeasurementList() throws Exception {
        UserInfo userInfoDomain = TestEntityFactory.createUserInfoDomain("john@gmail.com", "password123");
        userInfoRepository.save(userInfoDomain);
        User userDomain = TestEntityFactory.createUserDomain("John", Gender.MALE, Activity.EXTRA_ACTIVE, 53);
        userDomain.setUserInfo(userInfoDomain);
        userRepository.save(userDomain);
        Measurement height = TestEntityFactory.createHeight(183.0);
        height.setUser(userDomain);
        Measurement weight = TestEntityFactory.createMeasurementDomain(MeasureType.WEIGHT, 100D, Unit.KILOGRAMS);
        weight.setUser(userDomain);
        measurementRepository.saveAll(List.of(height, weight));

        long userId = userDomain.getId();
        mockMvc.perform(get("/user/{userId}/measurements", userId))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));
    }
}

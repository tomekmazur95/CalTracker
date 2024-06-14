package com.crud.api.service.integrationTests.controllers;

import com.crud.api.dto.RequestMeasurementDTO;
import com.crud.api.dto.RequestUserDTO;
import com.crud.api.entity.Measurement;
import com.crud.api.entity.UserInfo;
import com.crud.api.enums.*;
import com.crud.api.repository.MeasurementRepository;
import com.crud.api.repository.UserInfoRepository;
import com.crud.api.repository.UserRepository;
import com.crud.api.service.integrationTests.AppMySQLContainer;
import com.crud.api.service.integrationTests.DatabaseSetupExtension;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Objects;


@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(DatabaseSetupExtension.class)
public class UserControllerTestIT extends AppMySQLContainer {


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
    @WithMockUser(authorities = {"USER"})
    void shouldCreateUser() throws Exception {
        UserInfo userInfo = new UserInfo();
        userInfo.setEmail("john@gmail.com");
        userInfo.setPassword("password");
        userInfo.setRole(Role.USER);
        userInfoRepository.save(userInfo);

        RequestMeasurementDTO requestMeasurementDTO = new RequestMeasurementDTO();
        requestMeasurementDTO.setType(MeasureType.HEIGHT);
        requestMeasurementDTO.setValue(183.0);
        requestMeasurementDTO.setUnit(Unit.CENTIMETERS);

        RequestUserDTO requestUserDTO = new RequestUserDTO();
        requestUserDTO.setUserName("John");
        requestUserDTO.setGender(Gender.MALE);
        requestUserDTO.setActivity(Activity.MODERATELY_ACTIVE);
        requestUserDTO.setAge(30);
        requestUserDTO.setHeight(requestMeasurementDTO);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users/" + userInfo.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestUserDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.userName").value("John"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.gender").value("MALE"));

        Assertions.assertTrue(userRepository.existsByUserInfoId(userInfo.getId()));
        List<Measurement> userMeasurements = measurementRepository.findAll();
        Assertions.assertEquals(1, userMeasurements.size());
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    void shouldThrowExceptionWhenCreateUser() throws Exception {
        UserInfo userInfo = new UserInfo();
        userInfo.setEmail("john@gmail.com");
        userInfo.setPassword("password");
        userInfo.setRole(Role.USER);
        userInfoRepository.save(userInfo);

        RequestMeasurementDTO requestMeasurementDTO = new RequestMeasurementDTO();
        requestMeasurementDTO.setType(MeasureType.HEIGHT);
        requestMeasurementDTO.setValue(183.0);
        requestMeasurementDTO.setUnit(Unit.CENTIMETERS);

        RequestUserDTO requestUserDTO = new RequestUserDTO();
        requestUserDTO.setGender(Gender.MALE);
        requestUserDTO.setActivity(Activity.MODERATELY_ACTIVE);
        requestUserDTO.setAge(30);
        requestUserDTO.setHeight(requestMeasurementDTO);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .post("/users/" + userInfo.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestUserDTO)))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andReturn();

        String errorMessage = Objects.requireNonNull(result.getResolvedException()).getMessage();
        Assertions.assertTrue(result.getResolvedException() instanceof IllegalArgumentException);
        Assertions.assertEquals("The given fields must not be null", errorMessage);
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    void shouldReturnEmptyUsersList() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                .get("/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(0));

        Assertions.assertTrue(userRepository.findAll().isEmpty());
    }

    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}



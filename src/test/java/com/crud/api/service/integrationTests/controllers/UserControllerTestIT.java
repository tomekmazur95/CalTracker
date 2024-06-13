package com.crud.api.service.integrationTests.controllers;

import com.crud.api.dto.RequestMeasurementDTO;
import com.crud.api.dto.RequestUserDTO;
import com.crud.api.entity.UserInfo;
import com.crud.api.enums.*;
import com.crud.api.repository.MeasurementRepository;
import com.crud.api.repository.UserInfoRepository;
import com.crud.api.repository.UserRepository;
import com.crud.api.service.integrationTests.AppMySQLContainer;
import com.crud.api.service.integrationTests.DatabaseSetupExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;


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

    @BeforeEach
    public void setUp() {
        UserInfo userInfo = new UserInfo();
        userInfo.setEmail("john@gmail.com");
        userInfo.setPassword("password");
        userInfo.setRole(Role.USER);
        userInfoRepository.save(userInfo);
    }

    @AfterEach
    public void clean() {
        measurementRepository.deleteAll();
        userRepository.deleteAll();
        userInfoRepository.deleteAll();
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    void shouldCreateUser() throws Exception {
        RequestMeasurementDTO requestMeasurementDTO = new RequestMeasurementDTO(MeasureType.HEIGHT, 183.0, Unit.CENTIMETERS, null);
        RequestUserDTO requestUserDTO = new RequestUserDTO("John", 30, Gender.MALE, Activity.MODERATELY_ACTIVE, requestMeasurementDTO);

        UserInfo userInfo = userInfoRepository.findByEmail("john@gmail.com").orElseThrow();

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users/" + userInfo.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestUserDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
    }

    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}



package com.crud.api.integration.controller;


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
import com.crud.api.integration.AppMySQLContainer;
import com.crud.api.integration.DatabaseSetupExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Objects;

import static com.crud.api.integration.helper.TestEntityFactory.*;
import static com.crud.api.util.ConstantsUtils.CURRENT_WEIGHT;
import static com.crud.api.util.ConstantsUtils.ENERGY_GOAL;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(DatabaseSetupExtension.class)
public class GoalsControllerTestIT extends AppMySQLContainer {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserInfoRepository userInfoRepository;

    @Autowired
    MeasurementRepository measurementRepository;

    @AfterEach
    public void clean() {
        measurementRepository.deleteAll();
        userRepository.deleteAll();
        userInfoRepository.deleteAll();
    }

    @Test
    void shouldThrowForbiddenExceptionWhenUserIsUnauthorized() throws Exception {
        long userId = 10;
        MvcResult result = mockMvc.perform(get("/goals/{userId}", userId))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(status().isForbidden())
                .andReturn();

        int status = result.getResponse().getStatus();
        Assertions.assertEquals(403, status);
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    void shouldThrowUserNotFoundExceptionInFindUserGoalsMethod() throws Exception {
        long userId = 1;
        MvcResult result = mockMvc.perform(get("/goals/{userId}", userId))
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
    void shouldThrowMeasurementExceptionWhenCurrentWeightNotFound() throws Exception {
        UserInfo userInfoDomain = createUserInfoDomain("john@gmail.com", "password123");
        userInfoRepository.save(userInfoDomain);
        User userDomain = createUserDomain("John", Gender.MALE, Activity.EXTRA_ACTIVE, 53);
        userDomain.setUserInfo(userInfoDomain);
        userRepository.save(userDomain);

        long userId = userDomain.getId();

        MvcResult result = mockMvc.perform(get("/goals/{userId}", userId))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(status().isNotFound())
                .andReturn();

        int status = result.getResponse().getStatus();
        String errorMessage = Objects.requireNonNull(result.getResolvedException()).getMessage();
        Assertions.assertEquals(String.format("Measurement Type: %s not found for User with id %s", CURRENT_WEIGHT, userId), errorMessage);
        Assertions.assertEquals(404, status);
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    void shouldThrowMeasurementExceptionWhenGoalNotFound() throws Exception {
        UserInfo userInfoDomain = createUserInfoDomain("john@gmail.com", "password123");
        userInfoRepository.save(userInfoDomain);
        User userDomain = createUserDomain("John", Gender.MALE, Activity.EXTRA_ACTIVE, 53);
        userDomain.setUserInfo(userInfoDomain);
        userRepository.save(userDomain);
        Measurement currentWeight = createMeasurementDomain(MeasureType.CURRENT_WEIGHT, 85.0, Unit.KILOGRAMS);
        currentWeight.setUser(userDomain);
        measurementRepository.save(currentWeight);

        long userId = userDomain.getId();

        MvcResult result = mockMvc.perform(get("/goals/{userId}", userId))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(status().isNotFound())
                .andReturn();

        int status = result.getResponse().getStatus();
        String errorMessage = Objects.requireNonNull(result.getResolvedException()).getMessage();
        Assertions.assertEquals(String.format("Measurement Type: %s not found for User with id %s", ENERGY_GOAL, userId), errorMessage);
        Assertions.assertEquals(404, status);
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    void shouldThrowNutritionExceptionWhenNutritionNotFound() throws Exception {
        UserInfo userInfoDomain = createUserInfoDomain("john@gmail.com", "password123");
        userInfoRepository.save(userInfoDomain);
        User userDomain = createUserDomain("John", Gender.MALE, Activity.EXTRA_ACTIVE, 53);
        userDomain.setUserInfo(userInfoDomain);
        userRepository.save(userDomain);
        Measurement currentWeight = createMeasurementDomain(MeasureType.CURRENT_WEIGHT, 85.0, Unit.KILOGRAMS);
        currentWeight.setUser(userDomain);
        measurementRepository.save(currentWeight);
        Measurement energyGoal = createMeasurementDomain(MeasureType.ENERGY_SURPLUS, 4000.0, Unit.CALORIES);
        energyGoal.setUser(userDomain);
        measurementRepository.save(energyGoal);

        long userId = userDomain.getId();

        MvcResult result = mockMvc.perform(get("/goals/{userId}", userId))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(status().isNotFound())
                .andReturn();

        int status = result.getResponse().getStatus();
        String errorMessage = Objects.requireNonNull(result.getResolvedException()).getMessage();
        Assertions.assertEquals(String.format("Nutrition not found for Measurement with id %s", energyGoal.getId()), errorMessage);
        Assertions.assertEquals(404, status);
    }
}

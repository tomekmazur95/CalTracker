package com.crud.api.integration.controller;

import com.crud.api.entity.Measurement;
import com.crud.api.entity.User;
import com.crud.api.entity.UserInfo;
import com.crud.api.enums.Activity;
import com.crud.api.enums.Gender;
import com.crud.api.enums.MeasureType;
import com.crud.api.enums.Unit;
import com.crud.api.integration.AppMySQLContainer;
import com.crud.api.integration.DatabaseSetupExtension;
import com.crud.api.repository.MeasurementRepository;
import com.crud.api.repository.NutritionRepository;
import com.crud.api.repository.UserInfoRepository;
import com.crud.api.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Objects;

import static com.crud.api.integration.helper.TestEntityFactory.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(DatabaseSetupExtension.class)
public class CaloriesCalculatorControllerTestIT extends AppMySQLContainer {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserInfoRepository userInfoRepository;

    @Autowired
    MeasurementRepository measurementRepository;

    @Autowired
    NutritionRepository nutritionRepository;

    @AfterEach
    public void clean() {
        nutritionRepository.deleteAll();
        measurementRepository.deleteAll();
        userRepository.deleteAll();
        userInfoRepository.deleteAll();
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    public void shouldThrowIllegalArgumentException() throws Exception {
        Long userId = 1L;
        MvcResult result = mockMvc.perform(post("/user/{userId}/calories", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("goal", "SURPLUS"))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.goal").doesNotExist())
                .andReturn();

        String errorMessage = Objects.requireNonNull(result.getResolvedException()).getMessage();
        Assertions.assertTrue(result.getResolvedException() instanceof IllegalArgumentException);
        Assertions.assertEquals("Goal type not supported", errorMessage);
    }

    @Test
    void shouldThrowForbiddenExceptionWhenUserIsUnauthorized() throws Exception {
        Long userId = 43L;
        MvcResult result = mockMvc.perform(post("/user/{userId}/calories", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("goal", "SURPLUS"))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(status().isForbidden())
                .andReturn();
        int status = result.getResponse().getStatus();
        Assertions.assertEquals(HttpStatus.FORBIDDEN.value(), status);
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    void shouldCalculateCalorieWithTdeeGoal() throws Exception {
        UserInfo userInfoDomain = createUserInfoDomain("john@gmail.com", "password123");
        userInfoRepository.save(userInfoDomain);
        User userDomain = createUserDomain("John", Gender.MALE, Activity.EXTRA_ACTIVE, 53);
        userDomain.setUserInfo(userInfoDomain);
        userRepository.save(userDomain);
        Measurement currentWeight = createMeasurementDomain(MeasureType.CURRENT_WEIGHT, 85.0, Unit.KILOGRAMS);
        currentWeight.setUser(userDomain);
        measurementRepository.save(currentWeight);
        Measurement height = createHeight(190.0);
        height.setUser(userDomain);
        measurementRepository.save(height);

        long userId = userDomain.getId();

        MvcResult result = mockMvc.perform(post("/user/{userId}/calories", userId)
                        .param("goal", "ENERGY_TDEE"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.goal.value").value(3458))
                .andExpect(jsonPath("$.goal.type").value(MeasureType.ENERGY_TDEE.name()))
                .andExpect(jsonPath("$.goal.unit").value(Unit.CALORIES.name()))
                .andReturn();

        int status = result.getResponse().getStatus();
        Assertions.assertEquals(HttpStatus.OK.value(), status);
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    void shouldCalculateCalorieWithSurplusGoal() throws Exception {
        UserInfo userInfoDomain = createUserInfoDomain("john@gmail.com", "password123");
        userInfoRepository.save(userInfoDomain);
        User userDomain = createUserDomain("John", Gender.MALE, Activity.EXTRA_ACTIVE, 53);
        userDomain.setUserInfo(userInfoDomain);
        userRepository.save(userDomain);
        Measurement currentWeight = createMeasurementDomain(MeasureType.CURRENT_WEIGHT, 85.0, Unit.KILOGRAMS);
        currentWeight.setUser(userDomain);
        measurementRepository.save(currentWeight);
        Measurement height = createHeight(190.0);
        height.setUser(userDomain);
        measurementRepository.save(height);

        long userId = userDomain.getId();

        MvcResult result = mockMvc.perform(post("/user/{userId}/calories", userId)
                        .param("goal", "ENERGY_SURPLUS"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.goal.value").value(4150.0))
                .andExpect(jsonPath("$.goal.type").value(MeasureType.ENERGY_SURPLUS.name()))
                .andExpect(jsonPath("$.goal.unit").value(Unit.CALORIES.name()))
                .andExpect(jsonPath("$.nutrition").exists())
                .andExpect(jsonPath("$.activity").value(Activity.EXTRA_ACTIVE.name()))
                .andReturn();

        int status = result.getResponse().getStatus();
        Assertions.assertEquals(HttpStatus.OK.value(), status);
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    void shouldCalculateCalorieWithDeficitGoal() throws Exception {
        UserInfo userInfoDomain = createUserInfoDomain("john@gmail.com", "password123");
        userInfoRepository.save(userInfoDomain);
        User userDomain = createUserDomain("John", Gender.MALE, Activity.LIGHTLY_ACTIVE, 53);
        userDomain.setUserInfo(userInfoDomain);
        userRepository.save(userDomain);
        Measurement currentWeight = createMeasurementDomain(MeasureType.CURRENT_WEIGHT, 85.0, Unit.KILOGRAMS);
        currentWeight.setUser(userDomain);
        measurementRepository.save(currentWeight);
        Measurement height = createHeight(190.0);
        height.setUser(userDomain);
        measurementRepository.save(height);

        long userId = userDomain.getId();

        MvcResult result = mockMvc.perform(post("/user/{userId}/calories", userId)
                        .param("goal", "ENERGY_DEFICIT"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.goal.value").value(2002.0))
                .andExpect(jsonPath("$.goal.type").value(MeasureType.ENERGY_DEFICIT.name()))
                .andExpect(jsonPath("$.goal.unit").value(Unit.CALORIES.name()))
                .andExpect(jsonPath("$.nutrition").exists())
                .andExpect(jsonPath("$.activity").value(Activity.LIGHTLY_ACTIVE.name()))
                .andReturn();

        int status = result.getResponse().getStatus();
        Assertions.assertEquals(HttpStatus.OK.value(), status);
    }

}
package com.crud.api.integration.controller;


import com.crud.api.dto.RequestFoodDTO;
import com.crud.api.dto.RequestFoodFactDTO;
import com.crud.api.entity.Food;
import com.crud.api.entity.FoodFact;
import com.crud.api.entity.User;
import com.crud.api.entity.UserInfo;
import com.crud.api.enums.Activity;
import com.crud.api.enums.Gender;
import com.crud.api.integration.AppMySQLContainer;
import com.crud.api.integration.DatabaseSetupExtension;
import com.crud.api.integration.helper.TestJsonMapper;
import com.crud.api.repository.FoodFactRepository;
import com.crud.api.repository.FoodRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(DatabaseSetupExtension.class)
public class FoodControllerTestIT extends AppMySQLContainer {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    FoodRepository foodRepository;

    @Autowired
    UserInfoRepository userInfoRepository;

    @Autowired
    FoodFactRepository foodFactRepository;

    @AfterEach
    public void clean() {
        foodRepository.deleteAll();
        foodFactRepository.deleteAll();
        userRepository.deleteAll();
        userInfoRepository.deleteAll();
    }


    @Test
    void shouldThrowForbiddenExceptionWhenUserIsUnauthorized() throws Exception {
        long userId = 1;
        MvcResult result = mockMvc.perform(post("/foods")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestJsonMapper.asJsonString(new RequestFoodDTO()))
                        .param("userId", String.valueOf(userId)))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(status().isForbidden())
                .andReturn();

        int status = result.getResponse().getStatus();
        Assertions.assertEquals(HttpStatus.FORBIDDEN.value(), status);
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    void shouldThrowUserNotFoundExceptionWhenCreateFood() throws Exception {
        long userId = 1;
        MvcResult result = mockMvc.perform(post("/foods")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestJsonMapper.asJsonString(new RequestFoodDTO()))
                        .param("userId", String.valueOf(userId)))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(status().isNotFound())
                .andReturn();

        int status = result.getResponse().getStatus();
        String errorMessage = Objects.requireNonNull(result.getResolvedException()).getMessage();
        Assertions.assertEquals(String.format("User with id: %s not found", userId), errorMessage);
        Assertions.assertTrue(userRepository.findById(userId).isEmpty());
        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), status);
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    void shouldCreateFood() throws Exception {
        UserInfo userInfoDomain = createUserInfoDomain("john@gmail.com", "password123");
        userInfoRepository.save(userInfoDomain);
        User userDomain = createUserDomain("John", Gender.MALE, Activity.EXTRA_ACTIVE, 53);
        userDomain.setUserInfo(userInfoDomain);
        userRepository.save(userDomain);

        RequestFoodFactDTO requestFoodFactDTO = createRequestFoodFactDTO(100.0, 97.0, 5.0, 2.0, 11.0);
        RequestFoodDTO requestFoodDTO = createRequestFoodDTO("cottage cheese", "cottage cheese - description");
        requestFoodDTO.setRequestFoodFactDTO(requestFoodFactDTO);

        mockMvc.perform(post("/foods")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestJsonMapper.asJsonString(requestFoodDTO))
                        .param("userId", String.valueOf(userDomain.getId())))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value(requestFoodDTO.getName()))
                .andExpect(jsonPath("$.description").value(requestFoodDTO.getDescription()))
                .andExpect(jsonPath("$.responseFoodFactDTO.id").exists())
                .andExpect(jsonPath("$.responseFoodFactDTO.value").value(100))
                .andExpect(jsonPath("$.responseFoodFactDTO.carbohydrate").value(2.0));

        MvcResult result = mockMvc.perform(get("/foods")
                        .param("userId", String.valueOf(userDomain.getId())))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].name").value(requestFoodDTO.getName()))
                .andExpect(jsonPath("$[0].responseFoodFactDTO.calories").value(requestFoodDTO.getRequestFoodFactDTO().getCalories()))
                .andReturn();

        Assertions.assertFalse(foodRepository.findAllByUserId(userDomain.getId()).isEmpty());
        Assertions.assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    void shouldThrowExceptionWhenUserNotExistsWhenFindUserFoods() throws Exception {
        long userId = 7L;

        MvcResult result = mockMvc.perform(get("/foods")
                        .param("userId", String.valueOf(userId)))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(status().isNotFound())
                .andReturn();

        String errorMessage = Objects.requireNonNull(result.getResolvedException()).getMessage();
        Assertions.assertEquals(String.format("User with id: %s not found", userId), errorMessage);
        Assertions.assertTrue(userRepository.findById(userId).isEmpty());
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    void shouldReturnFoodList() throws Exception {
        UserInfo userInfoDomain = createUserInfoDomain("john@gmail.com", "password123");
        userInfoRepository.save(userInfoDomain);
        User userDomain = createUserDomain("John", Gender.MALE, Activity.EXTRA_ACTIVE, 53);
        userDomain.setUserInfo(userInfoDomain);
        userRepository.save(userDomain);
        Food foodDomain = createFood("cottage cheese", "cottage cheese - description");
        FoodFact foodFactDomain = createFoodFact(100.0, 97.0, 5.0, 2.0, 11.0);
        foodDomain.setFoodFact(foodFactDomain);
        foodDomain.setUser(userDomain);
        foodFactRepository.save(foodFactDomain);
        foodRepository.save(foodDomain);

        mockMvc.perform(get("/foods")
                        .param("userId", String.valueOf(userDomain.getId())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id").value(foodDomain.getId()))
                .andExpect(jsonPath("$[0].responseFoodFactDTO.id").value(foodFactDomain.getId()))
                .andExpect(jsonPath("$[1]").doesNotExist());
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    void shouldReturnEmptyFoodList() throws Exception {
        UserInfo userInfoDomain = createUserInfoDomain("john@gmail.com", "password123");
        userInfoRepository.save(userInfoDomain);
        User userDomain = createUserDomain("John", Gender.MALE, Activity.EXTRA_ACTIVE, 53);
        userDomain.setUserInfo(userInfoDomain);
        userRepository.save(userDomain);

        mockMvc.perform(get("/foods")
                        .param("userId", String.valueOf(userDomain.getId())))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));

        Assertions.assertTrue(foodRepository.findAllByUserId(userDomain.getId()).isEmpty());
    }
}

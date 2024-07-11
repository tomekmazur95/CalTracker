package com.crud.api.service.integration.controller;


import com.crud.api.dto.RequestFoodDTO;
import com.crud.api.dto.RequestFoodFactDTO;
import com.crud.api.entity.User;
import com.crud.api.entity.UserInfo;
import com.crud.api.enums.Activity;
import com.crud.api.enums.Gender;
import com.crud.api.repository.FoodFactRepository;
import com.crud.api.repository.FoodRepository;
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

import java.util.Objects;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
        Assertions.assertEquals(403, status);
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
        Assertions.assertEquals(404, status);
    }

    @Test
    @WithMockUser()
    void shouldCreateFood() throws Exception {
        UserInfo userInfoDomain = TestEntityFactory.createUserInfoDomain("john@gmail.com", "password123");
        userInfoRepository.save(userInfoDomain);
        User userDomain = TestEntityFactory.createUserDomain("John", Gender.MALE, Activity.EXTRA_ACTIVE, 53);
        userDomain.setUserInfo(userInfoDomain);
        userRepository.save(userDomain);

        RequestFoodFactDTO requestFoodFactDTO = TestEntityFactory.createRequestFoodFactDTO(100.0, 97.0, 5.0, 2.0, 11.0);
        RequestFoodDTO requestFoodDTO = TestEntityFactory.createRequestFoodDTO("cottage cheese", "cottage cheese - description");
        requestFoodDTO.setRequestFoodFactDTO(requestFoodFactDTO);

        mockMvc.perform(post("/foods")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestJsonMapper.asJsonString(requestFoodDTO))
                        .param("userId", String.valueOf(userDomain.getId())))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value(requestFoodDTO.getName()))
                .andExpect(jsonPath("$.description").value(requestFoodDTO.getDescription()))
                .andExpect(jsonPath("$.responseFoodFactDTO.id").exists())
                .andExpect(jsonPath("$.responseFoodFactDTO.value").value(100))
                .andExpect(jsonPath("$.responseFoodFactDTO.carbohydrate").value(2.0));

        Assertions.assertFalse(foodRepository.findAllByUserId(userDomain.getId()).isEmpty());
    }
}

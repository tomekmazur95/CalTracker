package com.crud.api.service.integration.controller;


import com.crud.api.dto.RequestMeasurementDTO;
import com.crud.api.repository.UserInfoRepository;
import com.crud.api.repository.UserRepository;
import com.crud.api.service.integration.AppMySQLContainer;
import com.crud.api.service.integration.DatabaseSetupExtension;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    @AfterEach
    public void clean() {
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
}

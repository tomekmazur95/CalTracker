package com.crud.api.integration.controller;

import com.crud.api.integration.AppMySQLContainer;
import com.crud.api.integration.DatabaseSetupExtension;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(DatabaseSetupExtension.class)
public class CaloriesCalculatorControllerTestIT extends AppMySQLContainer {

    @Autowired
    MockMvc mockMvc;

    @Test
    @WithMockUser(authorities = {"USER"})
    public void shouldThrowIllegalArgumentException() throws Exception {
        Long userId = 1L;
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/user/{userId}/calories", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("goal", "SURPLUS"))
                .andDo(print())
                .andExpect(mockMvc -> assertEquals(HttpStatus.Series.CLIENT_ERROR, HttpStatus.Series.resolve(mockMvc.getResponse().getStatus())))
                .andExpect(jsonPath("$.goal").doesNotExist())
                .andReturn();

        String errorMessage = Objects.requireNonNull(result.getResolvedException()).getMessage();
        Assertions.assertTrue(result.getResolvedException() instanceof IllegalArgumentException);
        Assertions.assertEquals("Goal type not supported", errorMessage);
    }
}
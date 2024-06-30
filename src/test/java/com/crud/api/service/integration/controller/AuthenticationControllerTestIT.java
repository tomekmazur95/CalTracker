package com.crud.api.service.integration.controller;

import com.crud.api.dto.RegisterRequest;
import com.crud.api.repository.UserInfoRepository;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(DatabaseSetupExtension.class)
public class AuthenticationControllerTestIT extends AppMySQLContainer {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserInfoRepository userInfoRepository;


    @AfterEach
    public void clean() {
        userInfoRepository.deleteAll();
    }

    @Test
    void shouldRegisterUser() throws Exception {
        String email = "john@gmail.com";
        String password = "password";
        RegisterRequest registerRequest = TestEntityFactory.createRegisterRequest(email, password);
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestJsonMapper.asJsonString(registerRequest)))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(status().isOk());
        Assertions.assertTrue(userInfoRepository.findByEmail(registerRequest.getEmail()).isPresent());
    }
}

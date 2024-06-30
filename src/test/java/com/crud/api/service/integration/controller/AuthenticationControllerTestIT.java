package com.crud.api.service.integration.controller;

import com.crud.api.dto.AuthenticationRequest;
import com.crud.api.dto.RegisterRequest;
import com.crud.api.entity.UserInfo;
import com.crud.api.enums.Role;
import com.crud.api.error.UserAlreadyExistsException;
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
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
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
public class AuthenticationControllerTestIT extends AppMySQLContainer {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @AfterEach
    public void clean() {
        userInfoRepository.deleteAll();
    }

    @Test
    void shouldRegisterUser() throws Exception {
        String email = "john@gmail.com";
        String password = "password";
        RegisterRequest registerRequest = TestEntityFactory.createRegisterRequest(email, password);

        Assertions.assertFalse(userInfoRepository.existsByEmail(email));

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestJsonMapper.asJsonString(registerRequest)))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());

        Assertions.assertTrue(userInfoRepository.existsByEmail(registerRequest.getEmail()));
    }

    @Test
    void shouldThrowExceptionWhenRegisterUserWithEmailAlreadyExists() throws Exception {
        String email = "john@gmail.com";
        String password = "password";
        UserInfo domain = TestEntityFactory.createUserInfoDomain(email, password);
        domain.setRole(Role.USER);
        userInfoRepository.save(domain);
        RegisterRequest registerRequest = TestEntityFactory.createRegisterRequest(email, password);

        Assertions.assertTrue(userInfoRepository.existsByEmail(email));

        MvcResult result = mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestJsonMapper.asJsonString(registerRequest)))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.token").doesNotExist())
                .andReturn();

        String errorMessage = Objects.requireNonNull(result.getResolvedException()).getMessage();
        Assertions.assertTrue(result.getResolvedException() instanceof UserAlreadyExistsException);
        Assertions.assertEquals(String.format("User with email: %s already exists", email), errorMessage);
    }

    @Test
    void shouldAuthenticatePositiveUser() throws Exception {
        String email = "john@gmail.com";
        String password = "password";
        UserInfo domain = TestEntityFactory.createUserInfoDomain(email, passwordEncoder.encode(password));
        domain.setRole(Role.USER);
        userInfoRepository.save(domain);

        Assertions.assertTrue(userInfoRepository.existsByEmail(email));

        AuthenticationRequest authenticationRequest = TestEntityFactory.createAuthenticationRequest(email, password);
        mockMvc.perform(post("/api/v1/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestJsonMapper.asJsonString(authenticationRequest)))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void shouldThrowExceptionWhenAuthenticateWithWrongPassword() throws Exception {
        String email = "john@gmail.com";
        String password = "password";
        UserInfo domain = TestEntityFactory.createUserInfoDomain(email, passwordEncoder.encode(password));
        domain.setRole(Role.USER);
        userInfoRepository.save(domain);

        Assertions.assertTrue(userInfoRepository.existsByEmail(email));

        String invalidPassword = "password123";
        AuthenticationRequest authenticationRequest = TestEntityFactory.createAuthenticationRequest(email, invalidPassword);

        MvcResult result = mockMvc.perform(post("/api/v1/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestJsonMapper.asJsonString(authenticationRequest)))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.token").doesNotExist())
                .andReturn();

        String errorMessage = Objects.requireNonNull(result.getResolvedException()).getMessage();
        Assertions.assertTrue(result.getResolvedException() instanceof BadCredentialsException);
        Assertions.assertEquals("Bad credentials", errorMessage);
    }

    @Test
    void shouldThrowExceptionWhenAuthenticateWithUserNotExists() throws Exception {
        String invalidEmail = "john@gmail.com";
        String password = "password";

        AuthenticationRequest authenticationRequest = TestEntityFactory.createAuthenticationRequest(invalidEmail, password);

        MvcResult result = mockMvc.perform(post("/api/v1/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestJsonMapper.asJsonString(authenticationRequest)))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.token").doesNotExist())
                .andReturn();

        String errorMessage = Objects.requireNonNull(result.getResolvedException()).getMessage();
        Assertions.assertTrue(result.getResolvedException() instanceof BadCredentialsException);
        Assertions.assertEquals("Bad credentials", errorMessage);
    }
}

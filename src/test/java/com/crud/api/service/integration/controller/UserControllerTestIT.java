package com.crud.api.service.integration.controller;

import com.crud.api.dto.RequestMeasurementDTO;
import com.crud.api.dto.RequestUserDTO;
import com.crud.api.entity.Measurement;
import com.crud.api.entity.User;
import com.crud.api.entity.UserInfo;
import com.crud.api.enums.Activity;
import com.crud.api.enums.Gender;
import com.crud.api.enums.MeasureType;
import com.crud.api.enums.Unit;
import com.crud.api.error.UserAlreadyExistsException;
import com.crud.api.error.UserNotFoundException;
import com.crud.api.repository.MeasurementRepository;
import com.crud.api.repository.UserInfoRepository;
import com.crud.api.repository.UserRepository;
import com.crud.api.service.integration.AppMySQLContainer;
import com.crud.api.service.integration.DatabaseSetupExtension;
import com.crud.api.service.integration.helper.TestEntityFactory;
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
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import static com.crud.api.enums.Gender.getGenderList;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


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
        UserInfo userInfo = TestEntityFactory.createUserInfoDomain("john@gmail.com", "password");
        userInfoRepository.save(userInfo);
        RequestMeasurementDTO height = TestEntityFactory.createRequestMeasurementDTO(MeasureType.HEIGHT, Unit.CENTIMETERS, 183.0);
        RequestUserDTO userDTO = TestEntityFactory.createRequestUserDTO("John", Gender.MALE, Activity.MODERATELY_ACTIVE, 30);
        userDTO.setHeight(height);

        mockMvc.perform(post("/users/{userInfoId}", userInfo.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userDTO)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.userName").value("John"))
                .andExpect(jsonPath("$.gender").value("MALE"))
                .andReturn();

        Assertions.assertTrue(userRepository.existsByUserInfoId(userInfo.getId()));
        List<Measurement> userMeasurements = measurementRepository.findAll();
        Assertions.assertEquals(1, userMeasurements.size());
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    void shouldThrowExceptionWhenCreateUserWithNullUserName() throws Exception {
        UserInfo userInfo = TestEntityFactory.createUserInfoDomain("john@gmail.com", "password");
        userInfoRepository.save(userInfo);
        RequestMeasurementDTO requestMeasurementDTO = TestEntityFactory.createRequestMeasurementDTO(MeasureType.HEIGHT, Unit.CENTIMETERS, 183.0);
        RequestUserDTO requestUserDTO = TestEntityFactory.createRequestUserDTO(null, Gender.MALE, Activity.MODERATELY_ACTIVE, 30);
        requestUserDTO.setHeight(requestMeasurementDTO);

        MvcResult result = mockMvc.perform(post("/users/{userInfoId}", userInfo.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestUserDTO)))
                .andExpect(status().is4xxClientError())
                .andExpect(status().isBadRequest())
                .andReturn();

        String errorMessage = Objects.requireNonNull(result.getResolvedException()).getMessage();
        Assertions.assertTrue(result.getResolvedException() instanceof IllegalArgumentException);
        Assertions.assertEquals("The given fields must not be null", errorMessage);
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    void shouldThrowExceptionWhenCreateUserWithUserInfoAlreadyExists() throws Exception {
        UserInfo userInfo = TestEntityFactory.createUserInfoDomain("john@gmail.com", "password");
        userInfoRepository.save(userInfo);
        User user = TestEntityFactory.createUserDomain("John", Gender.MALE, Activity.EXTRA_ACTIVE, 30);
        user.setUserInfo(userInfo);
        userRepository.save(user);
        Measurement height = TestEntityFactory.createHeight(183.0);
        height.setUser(user);
        measurementRepository.save(height);

        mockMvc.perform(get("/users/{id}", user.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.userName").value("John"))
                .andExpect(jsonPath("$.height").exists());

        RequestMeasurementDTO requestMeasurementDTO = TestEntityFactory.createRequestMeasurementDTO(MeasureType.HEIGHT, Unit.CENTIMETERS, 170.0);
        RequestUserDTO requestUserDTO = TestEntityFactory.createRequestUserDTO("Ann", Gender.FEMALE, Activity.LIGHTLY_ACTIVE, 20);
        requestUserDTO.setHeight(requestMeasurementDTO);

        MvcResult result = mockMvc.perform(post("/users/{userInfoId}", userInfo.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestUserDTO)))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(status().isConflict())
                .andReturn();

        String errorMessage = Objects.requireNonNull(result.getResolvedException()).getMessage();
        Assertions.assertTrue(result.getResolvedException() instanceof UserAlreadyExistsException);
        Assertions.assertEquals(String.format("User with user info id: %s already exists", userInfo.getId()), errorMessage);
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    void shouldThrowExceptionWhenCreateUserWithIncorrectUserInfoID() throws Exception {
        Long id = 10L;
        RequestMeasurementDTO requestMeasurementDTO = TestEntityFactory.createRequestMeasurementDTO(MeasureType.HEIGHT, Unit.CENTIMETERS, 170.0);
        RequestUserDTO requestUserDTO = TestEntityFactory.createRequestUserDTO("Ann", Gender.FEMALE, Activity.LIGHTLY_ACTIVE, 20);
        requestUserDTO.setHeight(requestMeasurementDTO);

        MvcResult result = mockMvc.perform(post("/users/{userInfoId}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestUserDTO)))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(status().isNotFound())
                .andReturn();

        String errorMessage = Objects.requireNonNull(result.getResolvedException()).getMessage();
        Assertions.assertTrue(result.getResolvedException() instanceof UserNotFoundException);
        Assertions.assertEquals(String.format("User with id: %s not found", id), errorMessage);
    }


    @Test
    @WithMockUser(authorities = {"USER"})
    void shouldReturnEmptyUsersList() throws Exception {

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        Assertions.assertTrue(userRepository.findAll().isEmpty());
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    void shouldReturnUsersList() throws Exception {
        int numberOfUsers = 2;
        prepareData(numberOfUsers);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(numberOfUsers));

        Assertions.assertEquals(numberOfUsers, userRepository.findAll().size());
        Assertions.assertEquals(numberOfUsers, measurementRepository.findAll().size());
        Assertions.assertEquals(numberOfUsers, measurementRepository.findAll().size());
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    void shouldReturnUserById() throws Exception {
        List<User> userList = prepareData(1);
        User user = userList.get(0);
        mockMvc.perform(get("/users/{userId}", user.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.userName").value(user.getUserName()))
                .andExpect(jsonPath("$.height").exists());

        Assertions.assertTrue(userRepository.existsByUserInfoId(user.getUserInfo().getId()));
        List<Measurement> userMeasurements = measurementRepository.findAll();
        Assertions.assertEquals(1, userMeasurements.size());
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    void shouldThrowExceptionWhenUserNotFoundInFindById() throws Exception {
        MvcResult result = mockMvc.perform(get("/users/{userId}", 10))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(status().isNotFound())
                .andReturn();

        String errorMessage = Objects.requireNonNull(result.getResolvedException()).getMessage();
        Assertions.assertTrue(result.getResolvedException() instanceof UserNotFoundException);
        Assertions.assertEquals(String.format("User with id: %s not found", 10), errorMessage);
    }


    private List<User> prepareData(int numberOfUsers) {
        List<User> userList = new ArrayList<>();
        for (int i = 0; i < numberOfUsers; i++) {
            StringBuilder password = new StringBuilder();
            for (int j = 0; j < 5; j++) {
                password.append(new Random().nextInt(9));
            }
            UserInfo userInfo = TestEntityFactory.createUserInfoDomain(i + "@gmail.com", password.toString());
            userInfoRepository.save(userInfo);
            List<Gender> genderList = getGenderList();
            User userDomain = TestEntityFactory.createUserDomain(
                    i + "name",
                    genderList.get(new Random().nextInt(genderList.size())),
                    Activity.getActivityList().get(new Random().nextInt(Activity.getActivityList().size())),
                    new Random().nextInt(18, 60));
            userDomain.setUserInfo(userInfo);
            userRepository.save(userDomain);

            Measurement heightDomain = TestEntityFactory.createHeight(new Random().nextDouble(160.0, 190.0));
            heightDomain.setUser(userDomain);
            measurementRepository.save(heightDomain);
            userList.add(userDomain);
        }
        return userList;
    }

    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}



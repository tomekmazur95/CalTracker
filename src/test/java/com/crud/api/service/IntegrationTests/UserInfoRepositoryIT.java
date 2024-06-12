package com.crud.api.service.IntegrationTests;

import com.crud.api.entity.UserInfo;
import com.crud.api.enums.Role;
import com.crud.api.repository.UserInfoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ExtendWith(DatabaseSetupExtension.class)
public class UserInfoRepositoryIT extends AppMySQLContainer{

    @Autowired
    UserInfoRepository userInfoRepository;


    @BeforeEach
    public void setUp() {
        UserInfo userInfo = new UserInfo();
        userInfo.setEmail("john2@gmail.com");
        userInfo.setPassword("password");
        userInfo.setRole(Role.USER);
        userInfoRepository.save(userInfo);
    }

    @AfterEach
    public void clean() {
        userInfoRepository.deleteAll();
    }

    @Test
    public void connectionEstablished() {
        assertThat(AppMySQLContainer.mySQLContainer.isCreated()).isTrue();
        assertThat(AppMySQLContainer.mySQLContainer.isRunning()).isTrue();
        System.out.println("Database Name: " + AppMySQLContainer.mySQLContainer.getDatabaseName());
        System.out.println("DriverClassName: " + AppMySQLContainer.mySQLContainer.getDriverClassName());
        System.out.println("JdbcUrl: " + AppMySQLContainer.mySQLContainer.getJdbcUrl());
        System.out.println("Password: " + AppMySQLContainer.mySQLContainer.getPassword());
        System.out.println("Database User: " + AppMySQLContainer.mySQLContainer.getUsername());
    }

    @Test
    public void shouldReturnUserInfoByEmail() {
        Optional<UserInfo> userInfo = userInfoRepository.findByEmail("john2@gmail.com");
        Assertions.assertTrue(userInfo.isPresent());
    }

    @Test
    public void shouldReturnEmptyOptionalWhenUserInfoNotFound() {
        Optional<UserInfo> user = userInfoRepository.findByEmail("ann@gmail.com");
        Assertions.assertTrue(user.isEmpty());
    }

    @Test
    public void shouldReturnFalseWhenUserInfoEmailNotExist() {
        boolean userExists = userInfoRepository.existsByEmail("mail@gmail.com");
        Assertions.assertFalse(userExists);
    }

    @Test
    public void shouldReturnTrueWhenUserInfoEmailExist() {
        boolean userExists = userInfoRepository.existsByEmail("john2@gmail.com");
        Assertions.assertTrue(userExists);
    }
}



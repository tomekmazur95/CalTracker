package com.crud.api.service.TestContainers;

import com.crud.api.entity.User;
import com.crud.api.entity.UserInfo;
import com.crud.api.enums.Activity;
import com.crud.api.enums.Gender;
import com.crud.api.enums.Role;
import com.crud.api.repository.UserInfoRepository;
import com.crud.api.repository.UserRepository;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@Testcontainers
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MyIntegrationTests {

    @Container
    @ServiceConnection
    static MySQLContainer<?> mySQL = new MySQLContainer<>("mysql:5.7");


    @Autowired
    UserRepository userRepository;
    @Autowired
    UserInfoRepository userInfoRepository;

    @Test
    void connectionEstablished() {
        assertThat(mySQL.isCreated()).isTrue();
        assertThat(mySQL.isRunning()).isTrue();
        System.out.println("Database Name: " + mySQL.getDatabaseName());
        System.out.println("DriverClassName" + mySQL.getDriverClassName());
        System.out.println("JdbcUrl" + mySQL.getJdbcUrl());
        System.out.println("Password" + mySQL.getPassword());

    }

    @BeforeEach
    void setUp() {
        UserInfo userInfo = new UserInfo(1L, "john@gmail.com", "password", Role.USER);
        userInfoRepository.save(userInfo);

        List<User> users = List.of(new User(1L, "John", 30, Gender.MALE, Activity.EXTRA_ACTIVE, userInfo));
        userRepository.saveAll(users);
    }

    @Test
    void shouldReturnUserInfoByEmail() {
        Optional<UserInfo> userInfo = userInfoRepository.findByEmail("john@gmail.com");
        Assertions.assertTrue(userInfo.isPresent());
    }

    @Test
    void shouldReturnEmptyOptionalWhenUserInfoNotFound() {
        Optional<UserInfo> user = userInfoRepository.findByEmail("ann@gmail.com");
        Assertions.assertTrue(user.isEmpty());
    }

    @Test
    void shouldReturnUserByUserName() {
        Optional<User> user = userRepository.findUserByUserInfoId(1L);
        Assertions.assertTrue(user.isPresent());
    }

    @Test
    void shouldReturnEmptyOptionalWhenUserNotFound() {
        Optional<User> user = userRepository.findById(2L);
        Assertions.assertTrue(user.isEmpty());
    }
}



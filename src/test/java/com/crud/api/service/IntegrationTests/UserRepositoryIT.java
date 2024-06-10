package com.crud.api.service.IntegrationTests;

import com.crud.api.entity.User;
import com.crud.api.entity.UserInfo;
import com.crud.api.enums.Activity;
import com.crud.api.enums.Gender;
import com.crud.api.enums.Role;
import com.crud.api.repository.UserInfoRepository;
import com.crud.api.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@ExtendWith(DatabaseSetupExtension.class)
public class UserRepositoryIT extends AppMySQLContainer {


    @Autowired
    UserRepository userRepository;

    @Autowired
    UserInfoRepository userInfoRepository;

    @BeforeEach
    public void setUp() {
        UserInfo userInfo = new UserInfo();
        userInfo.setEmail("Thomas@gmail.com");
        userInfo.setPassword("password");
        userInfo.setRole(Role.USER);
        userInfoRepository.save(userInfo);

        User user = new User();
        user.setUserName("Thomas");
        user.setGender(Gender.MALE);
        user.setActivity(Activity.EXTRA_ACTIVE);
        user.setUserInfo(userInfo);

        List<User> users = List.of(user);
        userRepository.saveAll(users);
    }

    @Test
    public void shouldReturnUsersList() {
        List<User> usersList = userRepository.findAll();
        Assertions.assertEquals(1, usersList.size());
    }
}

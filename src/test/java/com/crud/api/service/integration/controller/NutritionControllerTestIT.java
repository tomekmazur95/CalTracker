package com.crud.api.service.integration.controller;

import com.crud.api.dto.RequestNutritionDTO;
import com.crud.api.entity.Measurement;
import com.crud.api.entity.Nutrition;
import com.crud.api.entity.User;
import com.crud.api.entity.UserInfo;
import com.crud.api.enums.Activity;
import com.crud.api.enums.Gender;
import com.crud.api.enums.MeasureType;
import com.crud.api.enums.Unit;
import com.crud.api.repository.*;
import com.crud.api.service.integration.AppMySQLContainer;
import com.crud.api.service.integration.DatabaseSetupExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static com.crud.api.service.integration.helper.TestEntityFactory.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(DatabaseSetupExtension.class)
public class NutritionControllerTestIT extends AppMySQLContainer {

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

    @Autowired
    MeasurementRepository measurementRepository;

    @Autowired
    NutritionRepository nutritionRepository;

    @AfterEach
    public void clean() {
        nutritionRepository.deleteAll();
        measurementRepository.deleteAll();
        foodRepository.deleteAll();
        foodFactRepository.deleteAll();
        userRepository.deleteAll();
        userInfoRepository.deleteAll();
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    void shouldUpdateNutritions() throws Exception {
        UserInfo userInfoDomain = createUserInfoDomain("john@gmail.com", "password123");
        userInfoRepository.save(userInfoDomain);
        User userDomain = createUserDomain("John", Gender.MALE, Activity.EXTRA_ACTIVE, 53);
        userDomain.setUserInfo(userInfoDomain);
        userRepository.save(userDomain);
        Measurement height = createHeight(183d);
        height.setUser(userDomain);
        Measurement weight = createMeasurementDomain(MeasureType.CURRENT_WEIGHT, 105d, Unit.KILOGRAMS);
        weight.setUser(userDomain);
        measurementRepository.saveAll(List.of(height, weight));

        long userId = userDomain.getId();
        List<Measurement> measurementList = measurementRepository.findAllByUserId(userId);
        Assertions.assertEquals(2, measurementList.size());

        mockMvc.perform(post("/user/{userId}/calories", userDomain.getId())
                        .param("goal", "ENERGY_SURPLUS"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(status().isOk());

        Optional<Nutrition> beforeUpdate = nutritionRepository.findById(userId);
        Assertions.assertTrue(beforeUpdate.isPresent());
        Nutrition defaultNutrition = beforeUpdate.get();
        RequestNutritionDTO requestNutritionDTO = createRequestNutritionDTO(0.40, 0.35, 0.25);

        mockMvc.perform(put("/nutritions/{nutritionId}", defaultNutrition.getId())
                        .param("carbs", Double.toString(requestNutritionDTO.getCarbs()))
                        .param("protein", Double.toString(requestNutritionDTO.getProtein()))
                        .param("fat", Double.toString(requestNutritionDTO.getFat())))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(status().isOk());

        Optional<Nutrition> afterUpdate = nutritionRepository.findById(userId);
        Assertions.assertTrue(afterUpdate.isPresent());
        Nutrition nutritionAfterUpdate = afterUpdate.get();
        Assertions.assertEquals(0.40, nutritionAfterUpdate.getCarbohydratePercent());
        Assertions.assertEquals(0.35, nutritionAfterUpdate.getProteinPercent());
        Assertions.assertEquals(0.25, nutritionAfterUpdate.getFatPercent());
    }

}

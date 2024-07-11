package com.crud.api.service.integration.helper;

import com.crud.api.dto.*;
import com.crud.api.entity.*;
import com.crud.api.enums.*;

public class TestEntityFactory {

    public static UserInfo createUserInfoDomain(String email, String password) {
        UserInfo domain = new UserInfo();
        domain.setEmail(email);
        domain.setPassword(password);
        domain.setRole(Role.USER);
        return domain;
    }


    public static User createUserDomain(String userName, Gender gender, Activity activity, Integer age) {
        User domain = new User();
        domain.setUserName(userName);
        domain.setGender(gender);
        domain.setAge(age);
        domain.setActivity(activity);
        return domain;
    }

    public static Measurement createHeight(Double value) {
        Measurement domain = new Measurement();
        domain.setType(MeasureType.HEIGHT);
        domain.setValue(value);
        domain.setUnit(Unit.CENTIMETERS);
        return domain;
    }

    public static RequestMeasurementDTO createRequestMeasurementDTO(MeasureType measureType, Unit unit, Double value) {
        RequestMeasurementDTO dto = new RequestMeasurementDTO();
        dto.setType(measureType);
        dto.setUnit(unit);
        dto.setValue(value);
        return dto;
    }

    public static RequestUserDTO createRequestUserDTO(String userName, Gender gender, Activity activity, Integer age) {
        RequestUserDTO dto = new RequestUserDTO();
        dto.setUserName(userName);
        dto.setGender(gender);
        dto.setActivity(activity);
        dto.setAge(age);
        return dto;
    }

    public static RequestUserActivityDTO createRequestUserActivityDTO(Activity activity) {
        RequestUserActivityDTO dto = new RequestUserActivityDTO();
        dto.setActivity(activity);
        return dto;
    }

    public static RegisterRequest createRegisterRequest(String email, String password) {
        RegisterRequest dto = new RegisterRequest();
        dto.setEmail(email);
        dto.setPassword(password);
        return dto;
    }

    public static AuthenticationRequest createAuthenticationRequest(String email, String password) {
        AuthenticationRequest dto = new AuthenticationRequest();
        dto.setEmail(email);
        dto.setPassword(password);
        return dto;
    }

    public static Measurement createMeasurementDomain(MeasureType measureType, Double value, Unit unit) {
        Measurement domain = new Measurement();
        domain.setType(measureType);
        domain.setValue(value);
        domain.setUnit(unit);
        return domain;
    }

    public static RequestFoodFactDTO createRequestFoodFactDTO(Double value, Double calories, Double fat, Double carbo, Double protein) {
        RequestFoodFactDTO dto = new RequestFoodFactDTO();
        dto.setUnit(Unit.GRAMS);
        dto.setValue(value);
        dto.setCalories(calories);
        dto.setFat(fat);
        dto.setCarbohydrate(carbo);
        dto.setProtein(protein);
        return dto;
    }

    public static RequestFoodDTO createRequestFoodDTO(String name, String description) {
        RequestFoodDTO dto = new RequestFoodDTO();
        dto.setName(name);
        dto.setDescription(description);
        return dto;
    }

    public static Food createFood(String name, String description) {
        Food domain = new Food();
        domain.setName(name);
        domain.setDescription(description);
        return domain;
    }

    public static FoodFact createFoodFact(Double value, Double calories, Double fat, Double carbo, Double protein) {
        FoodFact domain = new FoodFact();
        domain.setUnit(Unit.GRAMS);
        domain.setValue(value);
        domain.setCalories(calories);
        domain.setFat(fat);
        domain.setCarbohydrate(carbo);
        domain.setProtein(protein);
        return domain;
    }
}

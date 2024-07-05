package com.crud.api.service.integration.helper;

import com.crud.api.dto.*;
import com.crud.api.entity.Measurement;
import com.crud.api.entity.User;
import com.crud.api.entity.UserInfo;
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
}

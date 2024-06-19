package com.crud.api.enums;

import java.util.List;

public enum Gender {
    MALE, FEMALE;

    public static List<Gender> getGenderList() {
        return List.of(MALE, FEMALE);
    }
}

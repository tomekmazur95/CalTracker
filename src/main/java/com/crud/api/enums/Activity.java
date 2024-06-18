package com.crud.api.enums;

import lombok.Getter;

import java.util.List;

@Getter
public enum Activity {
    SEDENTARY(1.2),          //little to no exercise + work a desk job
    LIGHTLY_ACTIVE(1.375),   //light exercise 1-3 days / week
    MODERATELY_ACTIVE(1.55), //moderate exercise 3-5 days / week
    VERY_ACTIVE(1.725),      //heavy exercise 6-7 days / week
    EXTRA_ACTIVE(1.9);       //very heavy exercise, hard labor job, training 2x / day

    private final double factor;

    Activity(double factor) {
        this.factor = factor;
    }

    public static List<Activity> getActivityList() {
        return List.of(SEDENTARY, LIGHTLY_ACTIVE, MODERATELY_ACTIVE, VERY_ACTIVE, EXTRA_ACTIVE);
    }
}

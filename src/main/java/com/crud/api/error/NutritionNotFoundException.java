package com.crud.api.error;

public class NutritionNotFoundException extends RuntimeException{

    public NutritionNotFoundException(String message) {
        super(message);
    }
}

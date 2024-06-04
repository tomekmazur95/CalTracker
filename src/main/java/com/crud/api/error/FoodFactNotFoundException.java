package com.crud.api.error;

public class FoodFactNotFoundException extends RuntimeException{

    public FoodFactNotFoundException(String message){
        super(message);
    }
}

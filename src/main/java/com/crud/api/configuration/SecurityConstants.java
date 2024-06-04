package com.crud.api.configuration;

public class SecurityConstants {

    private SecurityConstants() {
        // utility class
    }
    protected static final String[] PERMITTED_URLS = {
        "/api/v1/auth/**",
        "/swagger-ui/**",
        "/v3/**",
        "/userInfo/**",
    };
}

package com.crud.api.service.IntegrationTests;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class DatabaseSetupExtension implements BeforeAllCallback {

    @Override
    public void beforeAll(ExtensionContext extensionContext) {
        AppMySQLContainer.mySQLContainer
                .start();
    }
}

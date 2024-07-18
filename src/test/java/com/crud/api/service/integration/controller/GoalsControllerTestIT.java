package com.crud.api.service.integration.controller;


import com.crud.api.service.integration.AppMySQLContainer;
import com.crud.api.service.integration.DatabaseSetupExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(DatabaseSetupExtension.class)
public class GoalsControllerTestIT extends AppMySQLContainer {

    @Autowired
    MockMvc mockMvc;

    @Test
    void shouldThrowForbiddenExceptionWhenUserIsUnauthorized() throws Exception {
        long userId = 10;
        MvcResult result = mockMvc.perform(get("/goals/{userId}", userId))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(status().isForbidden())
                .andReturn();

        int status = result.getResponse().getStatus();
        Assertions.assertEquals(403, status);
    }

}

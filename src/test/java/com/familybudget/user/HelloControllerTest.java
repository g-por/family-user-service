package com.familybudget.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserServiceApplication.class)
class HelloControllerTest {

    @Autowired
    MockMvc mvc;

    @Test
    void helloEndpointWorks() throws Exception {
        mvc.perform(get("/users/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello from User Service!"));
    }
}

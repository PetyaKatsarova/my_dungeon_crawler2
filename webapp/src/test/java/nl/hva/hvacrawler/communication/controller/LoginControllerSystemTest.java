package nl.hva.hvacrawler.communication.controller;


import nl.hva.hvacrawler.business.domain.User;

import nl.hva.hvacrawler.persistence.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class LoginControllerSystemTest {
    private final User gebruiker1 = new User(1, "gebruiker1@example.com", "wachtwoord123",
            "zout123", true, "123456", "jwttoken123", 100);
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    LoginControllerSystemTest(MockMvc mockMvc) {
        super();
        this.mockMvc = mockMvc;
    }

    @Test
    void authenticateUserHandler() throws Exception {
        String email = "gebruiker1@example.com";
        String password = "wachtwoord123";

        // Create a JSON request body
        String requestBody = String.format("{\"email\": \"%s\", \"password\": \"%s\"}", email, password);

        // Send a POST request to /login
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post("/login")
                .contentType("application/json")
                .content(requestBody);

        // Perform the request and assert the response
        mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.content().string(org.hamcrest.Matchers.startsWith("Login failed")));
    }
}
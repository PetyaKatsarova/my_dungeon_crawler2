package nl.hva.hvacrawler.communication.controller;

import nl.hva.hvacrawler.business.domain.User;
import nl.hva.hvacrawler.business.service.LoginService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
@WebMvcTest(PasswordResetController.class)
class PasswordResetControllerTest {
    private MockMvc mockMvc;

    @MockBean
    private LoginService loginServiceTest;

    @Autowired
    public PasswordResetControllerTest(MockMvc mockMvc) {
        super();
        this.mockMvc = mockMvc;
    }

    @Test
    void resetLinkHandler() {

    }

    @Test
    void verifyAccountHandler() {
        Mockito
                .when(loginServiceTest.verifyAccount("dennis.de.kroes@hva.nl", "123456"))
                .thenReturn("token verified you can login");

        MockHttpServletRequestBuilder request =
                MockMvcRequestBuilders.post("/verify-account")
                        .param("email", "dennis.de.kroes@hva.nl")
                        .param("token", "123456");


        try {
            ResultActions response = mockMvc.perform(request);
            response.andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().string("token verified you can login"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
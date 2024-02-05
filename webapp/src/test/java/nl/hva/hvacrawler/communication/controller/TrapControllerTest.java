package nl.hva.hvacrawler.communication.controller;

import nl.hva.hvacrawler.business.domain.Trap;
import nl.hva.hvacrawler.business.service.TrapService;
import nl.hva.hvacrawler.communication.controller.TrapController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.Optional;

@WebMvcTest(TrapController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TrapControllerTest {

    @MockBean
    private TrapService trapService;
    private final MockMvc server;

    private Trap testTrap1;

    @Autowired
    public TrapControllerTest(MockMvc mockMvc) {
        super();
        this.server = mockMvc;
    }

    @BeforeEach
    public void initTest() {
        testTrap1 = new Trap(44, "mega_Trap", 50);
        testTrap1.setId(1);
        Mockito.when(trapService.getTrapById(44)).thenReturn(Optional.ofNullable(testTrap1));
    }

    @Test
    void getTrapByIdHandler() {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/trap/44");
        try {
            ResultActions response = server.perform(request);
            response
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn().getResponse().getContentAsString();
            String responseBody = response.andReturn().getResponse().getContentAsString();
            assertThat(responseBody).startsWith("{").endsWith("}").contains("mega_Trap");
        } catch (Exception e) {
            System.out.println(e.getMessage());

        }
    }
}

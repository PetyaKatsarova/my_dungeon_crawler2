package nl.hva.hvacrawler.communication.controller;

import nl.hva.hvacrawler.business.domain.Gold;
import nl.hva.hvacrawler.business.service.GoldService;
import nl.hva.hvacrawler.communication.controller.GoldController;
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
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

@WebMvcTest(GoldController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GoldControllerTest {

    @MockBean
    private GoldService goldService;
    private final MockMvc server;
    private Gold testGold1;

    @Autowired
    public GoldControllerTest(MockMvc mockMvc) {
        super();
        this.server = mockMvc;
    }

    @BeforeEach
    public void initTest() {
        testGold1 = new Gold(45, "11_Goldpieces", 11);
        testGold1.setId(1);
        Mockito.when(goldService.getGoldById(45)).thenReturn(Optional.ofNullable(testGold1));
    }

    @Test
    void getGoldByIdHandler() {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/gold/45");
        try {
            ResultActions response = server.perform(request);
            response
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn().getResponse().getContentAsString();
            String responseBody = response.andReturn().getResponse().getContentAsString();
            assertThat(responseBody).startsWith("{").endsWith("}").contains("11_Goldpieces");
        } catch (Exception e) {
            System.out.println(e.getMessage());

        }
    }
}
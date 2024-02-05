package nl.hva.hvacrawler.communication.controller;

import nl.hva.hvacrawler.business.domain.Crawler;
import nl.hva.hvacrawler.business.service.CrawlerService;
import nl.hva.hvacrawler.communication.controller.CrawlerController;
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
import static org.junit.jupiter.api.Assertions.*;
@WebMvcTest(CrawlerController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CrawlerControllerTest {

    @MockBean
    private CrawlerService crawlerService;
    private final MockMvc server;

    private Crawler testCrawler1;

    @Autowired
    public CrawlerControllerTest(MockMvc mockMvc) {
        super();
        this.server = mockMvc;
    }
    @BeforeEach
    public void initTest() {
        testCrawler1 = new Crawler("mario", 100, 50, 3, 6);
        testCrawler1.setIdCharacter(1);
        Mockito.when(crawlerService.getCrawlerById(1)).thenReturn(Optional.ofNullable(testCrawler1));
    }
    @Test
    void getCrawlerByIdHandler() {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/crawler/1");
        try {
            ResultActions response = server.perform(request);
            response
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn().getResponse().getContentAsString();
            String responseBody = response.andReturn().getResponse().getContentAsString();
            assertThat(responseBody).startsWith("{").endsWith("}").contains("mario");
        } catch (Exception e) {
            System.out.println(e.getMessage());

        }

    }
}

/**
 * Description:
 * Author: Petya Katsarova
 * Email: pskpetya@gmail.com
 * Created on: 15/08/2023 09:51
 */
package nl.hva.hvacrawler.communication.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.hva.hvacrawler.business.domain.Game;
import nl.hva.hvacrawler.business.domain.Crawler;
import nl.hva.hvacrawler.business.service.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import nl.hva.hvacrawler.business.domain.Game;
import nl.hva.hvacrawler.business.domain.User;
import nl.hva.hvacrawler.business.service.AuthorizationService;
import nl.hva.hvacrawler.business.service.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class GameControllerTest {
    @Autowired
    private MockMvc                 mockMvc;
    @MockBean
    private GameService             gameService;
    @MockBean
    private AuthorizationService    authorizationService;
    private User                    user;
    private Game                    game;
    private Crawler                 crawler;

    @BeforeEach
    public void setup() {
        user = new User();
        crawler = new Crawler();
        game = new Game(crawler,10, 10);

        when(authorizationService.validateJWTToken(anyString())).thenReturn(user);
        when(gameService.createGame(eq(user), anyInt(), anyInt())).thenReturn(game);
    }

    @Test
    public void testGameTester() throws Exception {
        //  it allows to isolate the behavior of the GameController from the GameService
        mockMvc.perform(get("/game/test")).andExpect(status().isOk());
    }

    @Test
    public void testCreateGame() throws Exception {
        MvcResult result = mockMvc.perform(get("/game/create")
                        .header("Authorization", "someJwtToken")
                        .param("rows", "10")
                        .param("columns", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Assert that the response body is not null
        assertNotNull(result.getResponse().getContentAsString());

        // Create an ObjectMapper and configure it to ignore unknown properties
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // Deserialize the JSON response into a Game object
        Game returnedGame = mapper.readValue(result.getResponse().getContentAsString(), Game.class);

        assertEquals(10, returnedGame.getRows());
        assertEquals(10, returnedGame.getColumns());
    }

    @Test
    public void testUnauthorizedCreateGame() throws Exception {
        when(authorizationService.validateJWTToken(anyString())).thenReturn(null);

        mockMvc.perform(get("/game/create")
                        .header("Authorization", "someInvalidJwtToken")
                        .param("rows", "10")
                        .param("columns", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}


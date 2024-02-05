package nl.hva.hvacrawler.communication.controller;

import nl.hva.hvacrawler.business.domain.Monster;
import nl.hva.hvacrawler.business.service.MonsterService;
import nl.hva.hvacrawler.communication.controller.MonsterController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MonsterController.class)
class MonsterControllerTest {

    @MockBean
    private MonsterService monsterService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new MonsterController(monsterService)).build();
    }

    @Test
    void testGetMonster_Success() throws Exception {
        int monsterId = 30;
        Monster mockMonster = new Monster("Mock Monster", 10, 20, 30);

        Mockito.when(monsterService.getMonsterById(monsterId)).thenReturn(Optional.of(mockMonster));

        mockMvc.perform(MockMvcRequestBuilders.get("/monsters/{id}", monsterId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Mock Monster"))
                .andExpect(jsonPath("$.healthPoints").value(10))
                .andExpect(jsonPath("$.gold").value(20))
                .andExpect(jsonPath("$.idCharacter").value(30));
    }

    @Test
    void testGetRandomMonster_Success() throws Exception {
        String expectedJsonResponse = "{\"idCharacter\": 3, \"name\": \"Bug\", \"healthPoints\": 10, \"gold\": 5}";

        Monster mockRandomMonster = new Monster("Bug", 10, 5, 3);
        Mockito.when(monsterService.getRandomMonster()).thenReturn(Optional.of(mockRandomMonster));

        mockMvc.perform(MockMvcRequestBuilders.get("/random-monster")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJsonResponse));
    }
}

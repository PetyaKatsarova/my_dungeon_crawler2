package nl.hva.hvacrawler.communication.controller;

import nl.hva.hvacrawler.business.domain.Potion;
import nl.hva.hvacrawler.business.service.PotionService;
import nl.hva.hvacrawler.communication.controller.PotionController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.Optional;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
* In de onderstaande code gebruik ik Mockito om de PotionService na te bootsen.
* Verder geef ik aan hoe deze zich moet gedragen wanneer de methoden ervan worden
* aangeroepen. Vervolgens gebruik ik MockMvc om HTTP GET request uit te voeren
* naar de controller enspoints en de verwachte statuscodes te checken.
*/

@WebMvcTest(PotionController.class)
public class PotionControllerTest {

    @MockBean
    private PotionService potionService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new PotionController(potionService)).build();
    }

    @Test
    void testGetPotionById() throws Exception {
        int potionId = 1;
        Potion testPotion = new Potion(); // Mockt een potion

        // Mockt de PotionService om getPotionById te testen.
        Mockito.when(potionService.getPotionById(potionId)).thenReturn(Optional.of(testPotion));

        // Doet een GET request /potion/{id} en verwacht status code 200
        System.out.println("Stap 1: Mocking PotionService.");
        ResultActions resultActions = mockMvc.perform(get("/potion/{id}", potionId)
                .contentType(MediaType.APPLICATION_JSON));
        System.out.println("Stap 2: GET request gedaan naar /potion/{id}.");

        resultActions.andExpect(status().isOk());
        System.out.println("Stap 3: Verwachtte statuscode gecontroleerd.");

        System.out.println("Test 'testGetPotionById' is voltooid.");
    }

    @Test
    void testGetRandomPotion() throws Exception {
        Potion testPotion = new Potion();

        Mockito.when(potionService.getRandomPotion()).thenReturn(Optional.of(testPotion));

        // Doet een GET request /random-potion en verwacht status code 200
        System.out.println("Stap 1: Mocking PotionService voor getRandomPotion.");
        ResultActions resultActions = mockMvc.perform(get("/random-potion")
                .contentType(MediaType.APPLICATION_JSON));
        System.out.println("Stap 2: GET request gedaan naar /random-potion.");

        resultActions.andExpect(status().isOk());
        System.out.println("Stap 3: Verwachtte statuscode gecontroleerd.");

        System.out.println("Test 'testGetRandomPotion' is voltooid.");
    }
}

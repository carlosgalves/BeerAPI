package beer.api.beer.query;

import beer.api.beer.query.controllers.BeerQueryController;
import beer.api.beer.query.model.Beer;
import beer.api.beer.query.services.BeerQueryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(BeerQueryController.class)
public class BeerQueryControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BeerQueryService beerQueryService;

    List<Beer> beers = new ArrayList<>();

    @BeforeEach
    void setUp() {
        Beer beer1 = new Beer();
        beer1.setId(UUID.randomUUID());
        beer1.setName("Beer1");
        beer1.setBrewery("Brewery1");
        beer1.setType("Lager");
        beer1.setImage("");
        beer1.setDescription("");
        beer1.setAbv(5.0f);
        beer1.setCountryIso("xx");
        beer1.setTags(List.of(""));
        beer1.setEan("00000000");

        Beer beer2 = new Beer();
        beer2.setId(UUID.randomUUID());
        beer2.setName("Beer2");
        beer2.setBrewery("Brewery2");
        beer2.setType("IPA");
        beer2.setImage("");
        beer2.setDescription("");
        beer2.setAbv(7.9f);
        beer2.setCountryIso("xx");
        beer2.setTags(List.of(""));
        beer2.setEan("00000001");

        beers = List.of(beer1, beer2);
    }

    @Test
    void getAllBeers_Success() throws Exception {
        when(beerQueryService.getAllBeers()).thenReturn(beers);

        mockMvc.perform(get("/beers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Beer1"))
                .andExpect(jsonPath("$[0].brewery").value("Brewery1"))
                .andExpect(jsonPath("$[0].type").value("Lager"))
                .andExpect(jsonPath("$[0].image").value(""))
                .andExpect(jsonPath("$[0].description").value(""))
                .andExpect(jsonPath("$[0].abv").value(5.0f))
                .andExpect(jsonPath("$[0].countryIso").value("xx"))
                .andExpect(jsonPath("$[0].tags").value(""))
                .andExpect(jsonPath("$[0].ean").value("00000000"))
                .andExpect(jsonPath("$[1].name").value("Beer2"))
                .andExpect(jsonPath("$[1].brewery").value("Brewery2"))
                .andExpect(jsonPath("$[1].type").value("IPA"))
                .andExpect(jsonPath("$[1].image").value(""))
                .andExpect(jsonPath("$[1].description").value(""))
                .andExpect(jsonPath("$[1].abv").value(7.9f))
                .andExpect(jsonPath("$[1].countryIso").value("xx"))
                .andExpect(jsonPath("$[1].tags").value(""))
                .andExpect(jsonPath("$[1].ean").value("00000001"));

        verify(beerQueryService, times(1)).getAllBeers();
    }

    @Test
    void getBeerById_Success() throws Exception {
        String beerId = beers.getFirst().getId().toString();
        when(beerQueryService.getBeerById(beerId)).thenReturn(beers.getFirst());

        mockMvc.perform(get("/beers/{id}", beerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(beerId))
                .andExpect(jsonPath("$.name").value("Beer1"))
                .andExpect(jsonPath("$.brewery").value("Brewery1"))
                .andExpect(jsonPath("$.type").value("Lager"))
                .andExpect(jsonPath("$.image").value(""))
                .andExpect(jsonPath("$.description").value(""))
                .andExpect(jsonPath("$.abv").value(5.0f))
                .andExpect(jsonPath("$.countryIso").value("xx"))
                .andExpect(jsonPath("$.tags").value(""))
                .andExpect(jsonPath("$.ean").value("00000000"));

        verify(beerQueryService, times(1)).getBeerById(beerId);
    }

    @Test
    void getBeerById_NotFound() throws Exception {
        String beerId = "1a2b3c4d-aaaa-bbbb-0000-000abc000cba";
        when(beerQueryService.getBeerById(beerId)).thenThrow(new RuntimeException("Beer not found"));

        mockMvc.perform(get("/beers/{id}", beerId))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Beer not found"));

        verify(beerQueryService, times(1)).getBeerById(beerId);
    }
}

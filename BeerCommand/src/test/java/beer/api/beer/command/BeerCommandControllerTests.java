package beer.api.beer.command;


import beer.api.beer.command.controllers.BeerCommandController;
import beer.api.beer.command.dto.CreateBeerRequest;
import beer.api.beer.command.exceptions.BeerNotFoundException;
import beer.api.beer.command.exceptions.DuplicateEanException;
import beer.api.beer.command.services.BeerCommandService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;


@WebMvcTest(BeerCommandController.class)
public class BeerCommandControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BeerCommandService beerCommandService;

    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();

    private CreateBeerRequest createBeerRequest;

    private final String BEER_ENDPOINT = "/beers";

    private UUID beerId;


    @BeforeEach
    void setUp() {
        createBeerRequest = new CreateBeerRequest();
        createBeerRequest.setName("Test Beer");
        createBeerRequest.setBrewery("Brewery");
        createBeerRequest.setType("Lager");
        createBeerRequest.setImage("");
        createBeerRequest.setDescription("Test Description");
        createBeerRequest.setAbv(5.0f);
        createBeerRequest.setCountryIso("xx");
        createBeerRequest.setTags(Collections.singletonList(""));
        createBeerRequest.setEan("0123456789");

        beerId = UUID.randomUUID();
    }

    @Test
    void createBeer_Success() throws Exception {

        mockMvc.perform(post(BEER_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createBeerRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Beer created successfully!"));

        verify(beerCommandService, times(1)).createBeer(any(CreateBeerRequest.class));
    }

    // TODO: this should return a "request is empty" message
    @Test
    void createBeer_BadRequest_EmptyRequest() throws Exception {
        CreateBeerRequest emptyRequest = new CreateBeerRequest();

        mockMvc.perform(post(BEER_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emptyRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"));

        verify(beerCommandService, never()).createBeer(any(CreateBeerRequest.class));
    }

    @Test
    void createBeer_BadRequest_NameIsBlank() throws Exception {
        createBeerRequest.setName("");

        mockMvc.perform(post(BEER_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createBeerRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Invalid parameters"))
                .andExpect(jsonPath("$.errors.name").value("Name cannot be blank"));

        verify(beerCommandService, never()).createBeer(any(CreateBeerRequest.class));
    }

    // TODO: test if brewery exists

    @Test
    void createBeer_BadRequest_BreweryIsBlank() throws Exception {
        createBeerRequest.setBrewery("");

        mockMvc.perform(post(BEER_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createBeerRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Invalid parameters"))
                .andExpect(jsonPath("$.errors.brewery").value("Brewery cannot be blank"));

        verify(beerCommandService, never()).createBeer(any(CreateBeerRequest.class));
    }

    // TODO: test if type exists

    @Test
    void createBeer_BadRequest_TypeIsBlank() throws Exception {
        createBeerRequest.setType("");

        mockMvc.perform(post(BEER_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createBeerRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Invalid parameters"))
                .andExpect(jsonPath("$.errors.type").value("Type cannot be blank"));

        verify(beerCommandService, never()).createBeer(any(CreateBeerRequest.class));
    }

    @Test
    void createBeer_BadRequest_DescriptionTooLong() throws Exception {
        createBeerRequest.setDescription("This description is intentionally made longer than 100 characters. It should cause a validation error when creating a beer.");

        mockMvc.perform(post(BEER_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createBeerRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Invalid parameters"))
                .andExpect(jsonPath("$.errors.description").value("Description cannot exceed 100 characters"));

        verify(beerCommandService, never()).createBeer(any(CreateBeerRequest.class));
    }

    // TODO: test for null/empty ABV
    @Test
    void createBeer_BadRequest_InvalidABV() throws Exception {
        createBeerRequest.setAbv(-1f);

        mockMvc.perform(post(BEER_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createBeerRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Invalid parameters"))
                .andExpect(jsonPath("$.errors.abv").value("ABV cannot be lower than 0"));

        verify(beerCommandService, never()).createBeer(any(CreateBeerRequest.class));
    }

    // TODO: test countryIsos with lower or upper case ("xX", "Xx", "xx", "XX")

    @ParameterizedTest
    @ValueSource(strings = {"", "X", "XXX"}) // 0 letters, 1 letter, 3 letters
    void createBeer_BadRequest_InvalidCountryISO(String invalidCountryIso) throws Exception {
        createBeerRequest.setCountryIso(invalidCountryIso);

        mockMvc.perform(post(BEER_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createBeerRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Invalid parameters"))
                .andExpect(jsonPath("$.errors.countryIso").value("Country ISO must consist of exactly two letters according to ISO-3166"));

        verify(beerCommandService, never()).createBeer(any(CreateBeerRequest.class));
    }

    // TODO: test Tags

    // TODO: test images

    // TODO: test EAN (format, length)
    @Test
    void createBeer_BadRequest_InvalidEAN() throws Exception {
        createBeerRequest.setEan("");

        mockMvc.perform(post(BEER_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createBeerRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Invalid parameters"))
                .andExpect(jsonPath("$.errors.ean").value("EAN cannot be blank"));

        verify(beerCommandService, never()).createBeer(any(CreateBeerRequest.class));
    }

    @Test
    void createBeer_BadRequest_DuplicateEAN() throws Exception {

        // Simulate that the service throws an exception when trying to create a beer with an existing EAN
        doThrow(new DuplicateEanException("EAN already exists")).when(beerCommandService).createBeer(any(CreateBeerRequest.class));

        mockMvc.perform(post(BEER_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createBeerRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Duplicate EAN"))
                .andExpect(jsonPath("$.errors.ean").value("EAN already exists"));

        verify(beerCommandService, times(1)).createBeer(any(CreateBeerRequest.class));
    }

    @Test
    void updateBeer_Success() throws Exception {
        Map<String, Object> updates = Map.of(
                "name", "Updated IPA",
                "abv", 5.5
        );

        doNothing().when(beerCommandService).patchBeer(any(UUID.class), eq(updates));

        mockMvc.perform(patch("/beers/{id}", beerId)
                        .contentType("application/json")
                        .content("{\"name\":\"Updated IPA\", \"abv\": 5.5}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Beer updated successfully!"));

        verify(beerCommandService, times(1)).patchBeer(eq(beerId), eq(updates));
    }

    @Test
    void updateBeer_NotFound() throws Exception {
        doThrow(new BeerNotFoundException(beerId)).when(beerCommandService).patchBeer(eq(beerId), any(Map.class));

        mockMvc.perform(patch("/beers/{id}", beerId)
                        .contentType("application/json")
                        .content("{\"name\":\"Non-existent IPA\", \"abv\": 6.0}"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Couldn't find beer with ID: " + beerId));

        verify(beerCommandService, times(1)).patchBeer(eq(beerId), any(Map.class));
    }

    // TODO: test updating with invalid parameters

    @Test
    void deleteBeer_Success() throws Exception {
        doNothing().when(beerCommandService).deleteBeer(eq(beerId));

        mockMvc.perform(delete("/beers/{id}", beerId))
                .andExpect(status().isNoContent())
                .andExpect(content().string("Beer deleted successfully!"));

        verify(beerCommandService, times(1)).deleteBeer(eq(beerId));
    }

    @Test
    void deleteBeer_NotFound() throws Exception {
        doThrow(new BeerNotFoundException(beerId)).when(beerCommandService).deleteBeer(eq(beerId));

        mockMvc.perform(delete("/beers/{id}", beerId))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Couldn't find beer with ID: " + beerId));

        verify(beerCommandService, times(1)).deleteBeer(eq(beerId));
    }

}
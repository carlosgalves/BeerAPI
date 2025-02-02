package beer.api.beer.command.controllers;


import beer.api.beer.command.dto.CreateBeerRequest;
import beer.api.beer.command.services.BeerCommandService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/beers")
public class BeerCommandController {

    private final BeerCommandService beerCommandService;

    @Autowired
    public BeerCommandController(BeerCommandService beerCommandService) {
        this.beerCommandService = beerCommandService;
    }

    @PostMapping
    public ResponseEntity<?> createBeer(@RequestBody @Valid CreateBeerRequest request) throws RuntimeException {
        beerCommandService.createBeer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Beer created successfully!");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> patchBeer(@PathVariable("id") UUID id, @RequestBody @Valid Map<String, Object> updates) throws IllegalArgumentException, RuntimeException {
        beerCommandService.patchBeer(id, updates);
        return ResponseEntity.status(HttpStatus.OK).body("Beer updated successfully!");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBeer(@PathVariable("id") UUID id) throws RuntimeException{
        beerCommandService.deleteBeer(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Beer deleted successfully!");
    }
}
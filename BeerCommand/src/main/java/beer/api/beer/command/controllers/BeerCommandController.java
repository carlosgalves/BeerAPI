package beer.api.beer.command.controllers;


import beer.api.beer.command.dto.CreateBeerRequest;
import beer.api.beer.command.services.BeerCommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<?> createBeer(@RequestBody CreateBeerRequest request) throws ExecutionException, InterruptedException {
        beerCommandService.createBeer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Beer created successfully!");

    }
}
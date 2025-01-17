package beer.api.beer.query.controllers;


import beer.api.beer.query.model.Beer;
import beer.api.beer.query.services.BeerQueryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BeerQueryController {

    private final BeerQueryService beerQueryService;

    public BeerQueryController(BeerQueryService beerQueryService) {
        this.beerQueryService = beerQueryService;
    }

    @GetMapping("/beers")
    public List<Beer> getAllBeers() {
        return beerQueryService.getAllBeers();
    }

    @GetMapping("/beers/{id}")
    public Beer getBeerById(@PathVariable String id) {
        return beerQueryService.getBeerById(id);
    }
}
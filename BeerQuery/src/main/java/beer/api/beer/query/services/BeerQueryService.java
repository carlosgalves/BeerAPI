package beer.api.beer.query.services;


import beer.api.beer.query.model.Beer;
import beer.api.beer.query.repositories.BeerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BeerQueryService {

    private final BeerRepository beerRepository;

    public BeerQueryService(BeerRepository beerRepository) {
        this.beerRepository = beerRepository;
    }

    public List<Beer> getAllBeers() {
        return beerRepository.findAll();
    }

    public Beer getBeerById(String id) {
        Optional<Beer> beer = beerRepository.findById(id);
        return beer.orElseThrow(() -> new RuntimeException("Beer not found"));
    }
}

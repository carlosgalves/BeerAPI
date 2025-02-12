package beer.api.beer.query.services;


import beer.api.beer.query.model.Beer;
import beer.api.beer.query.repositories.BeerRepository;
import beer.api.beer.query.exceptions.BeerNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

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
        return beerRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new BeerNotFoundException(UUID.fromString(id)));
    }
}

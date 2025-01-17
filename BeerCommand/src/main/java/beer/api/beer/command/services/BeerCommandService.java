package beer.api.beer.command.services;


import beer.api.beer.command.dto.CreateBeerRequest;

public interface BeerCommandService {
    public void createBeer(CreateBeerRequest request);
}
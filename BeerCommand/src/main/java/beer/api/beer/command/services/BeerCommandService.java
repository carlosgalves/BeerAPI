package beer.api.beer.command.services;


import beer.api.beer.command.dto.CreateBeerRequest;

import java.util.Map;
import java.util.UUID;

public interface BeerCommandService {
    public void createBeer(CreateBeerRequest request);

    public void patchBeer(UUID id, Map<String, Object> updates);

    public void deleteBeer(UUID id);
}
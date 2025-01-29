package beer.api.beer.command.services;


import beer.api.beer.command.dto.CreateBeerRequest;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface BeerCommandService {
    void createBeer(CreateBeerRequest request);

    void createMultipleBeer(List<CreateBeerRequest> request);

    void patchBeer(UUID id, Map<String, Object> updates);

    void deleteBeer(UUID id);


}
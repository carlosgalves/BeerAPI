package beer.api.beer.command.services;


import beer.api.beer.command.dto.CreateBeerRequest;
import beer.api.beer.command.events.BeerCreatedEvent;
import beer.api.beer.command.events.BeerDeletedEvent;
import beer.api.beer.command.model.Beer;
import beer.api.beer.command.repositories.BeerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
public class BeerCommandServiceImplementation implements BeerCommandService {

    private final BeerRepository beerRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    public BeerCommandServiceImplementation(BeerRepository beerRepository, KafkaTemplate<String, Object> kafkaTemplate) {
        this.beerRepository = beerRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void createBeer(CreateBeerRequest request) {
        Beer beer = new Beer();
        beer.setName(request.getName());
        beer.setBrewery(request.getBrewery());
        beer.setType(request.getType());
        beer.setImage(request.getImage());
        beer.setDescription(request.getDescription());
        beer.setAbv(request.getAbv());
        beer.setCountryIso(request.getCountryIso());
        beer.setEan(request.getEan());
        beer.setTags(request.getTags());
        beer.setOverallRating(0f);
        beer.setAromaRating(0f);
        beer.setTasteRating(0f);
        beer.setAfterTasteRating(0f);

        beerRepository.save(beer);
        log.info("Beer created: {}", beer);

        BeerCreatedEvent event = new BeerCreatedEvent(
            beer.getId().toString(),
            beer.getName(),
            beer.getBrewery(),
            beer.getType(),
            beer.getImage(),
            beer.getDescription(),
            beer.getAbv(),
            beer.getCountryIso(),
            beer.getEan(),
            beer.getTags(),
            0f,
            0f,
            0f,
            0f
        );

        kafkaTemplate.send("beer-topic", event);
    }

    @Override
    public void patchBeer(UUID id, Map<String, Object> updates) {
        Beer beer = beerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Beer not found"));


    }

    @Override
    public void deleteBeer(UUID id) {
        Beer beer = beerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Beer not found"));

        beerRepository.deleteById(id);

        BeerDeletedEvent event = new BeerDeletedEvent(id);

        kafkaTemplate.send("beer-topic", event);
        log.info("Deleted beer with id: {}", id);
    }
}
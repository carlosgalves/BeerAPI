package beer.api.beer.command.services;


import beer.api.beer.command.dto.CreateBeerRequest;
import beer.api.beer.command.events.BeerCreatedEvent;
import beer.api.beer.command.events.BeerDeletedEvent;
import beer.api.beer.command.events.BeerUpdatedEvent;
import beer.api.beer.command.model.Beer;
import beer.api.beer.command.repositories.BeerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
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
        log.info("Sent event: {}", event);
    }

    @Override
    public void patchBeer(UUID id, Map<String, Object> updates) {
        Beer beer = beerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Beer not found"));

        BeerUpdatedEvent.BeerUpdatedEventBuilder eventBuilder = BeerUpdatedEvent.builder()
                        .id(String.valueOf(id));

        updates.forEach((key, value) -> {
            switch (key) {
                case "name":
                    beer.setName((String) value);
                    eventBuilder.name((String) value);
                    break;
                case "brewery":
                    beer.setBrewery((String) value);
                    eventBuilder.brewery((String) value);
                    break;
                case "type":
                    beer.setType((String) value);
                    eventBuilder.type((String) value);
                    break;
                case "image":
                    beer.setImage((String) value);
                    eventBuilder.image((String) value);
                    break;
                case "description":
                    beer.setDescription((String) value);
                    eventBuilder.description((String) value);
                    break;
                case "abv":
                    if (value instanceof Double) {
                        beer.setAbv(((Double) value).floatValue());
                        eventBuilder.abv(((Double) value).floatValue());
                    } else if (value instanceof Integer) {
                        beer.setAbv(((Integer) value).floatValue());
                        eventBuilder.abv(((Integer) value).floatValue());
                    } else if (value instanceof Float) {
                        beer.setAbv((Float) value);
                        eventBuilder.abv((Float) value);
                    }
                    break;
                case "countryIso":
                    beer.setCountryIso((String) value);
                    eventBuilder.countryIso((String) value);
                    break;
                case "tags":
                    beer.setTags((List<String>) value);
                    eventBuilder.tags((List<String>) value);
                    break;
                case "ean":
                    beer.setEan((String) value);
                    eventBuilder.ean((String) value);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid field: " + key);
            }
        });

        beerRepository.save(beer);
        log.info("Modified beer data: {}", beer);

        BeerUpdatedEvent event = eventBuilder.build();

        kafkaTemplate.send("beer-topic", event);
        log.info("Sent event: {}", event);
    }

    @Override
    public void deleteBeer(UUID id) {
        beerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Beer not found"));

        beerRepository.deleteById(id);
        log.info("Deleted beer with id: {}", id);

        BeerDeletedEvent event = new BeerDeletedEvent(id);

        kafkaTemplate.send("beer-topic", event);
        log.info("Sent event: {}", event);
    }
}
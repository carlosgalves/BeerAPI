package beer.api.beer.query.listeners;


import beer.api.beer.query.events.BeerCreatedEvent;
import beer.api.beer.query.model.Beer;
import beer.api.beer.query.repositories.BeerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BeerCreatedEventListener {

    private final BeerRepository beerRepository;

    @Autowired
    public BeerCreatedEventListener(BeerRepository beerRepository) {
        this.beerRepository = beerRepository;
    }

    @KafkaListener(topics = "beer-topic", groupId = "beer-consumers", containerFactory = "kafkaListenerContainerFactory")
    public void handleBeerCreatedEvent(BeerCreatedEvent event) {

        try {
            log.info("Received event: {}", event);

            Beer beer = new Beer(
                    event.getId(),
                    event.getName(),
                    event.getBrewery(),
                    event.getType(),
                    event.getImage(),
                    event.getDescription(),
                    event.getAbv(),
                    event.getCountryIso(),
                    event.getEan(),
                    event.getTags(),
                    event.getOverallRating(),
                    event.getAromaRating(),
                    event.getTasteRating(),
                    event.getAfterTasteRating()
            );

            beerRepository.save(beer);
            log.info("Beer saved to query database: {}", beer.getName());
        } catch (Exception e) {
            log.error("Error processing beer event: {}", e.getMessage(), e);
            throw new RuntimeException("Error processing beer event", e);
        }
    }
}


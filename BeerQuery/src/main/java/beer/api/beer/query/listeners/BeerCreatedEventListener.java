package beer.api.beer.query.listeners;


import beer.api.beer.query.events.BeerCreatedEvent;
import beer.api.beer.query.model.Beer;
import beer.api.beer.query.repositories.BeerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BeerCreatedEventListener {

    private final BeerRepository beerRepository;

    @Autowired
    public BeerCreatedEventListener(BeerRepository beerRepository) {
        this.beerRepository = beerRepository;
    }

    @Value("${spring.kafka.event.key.beer.created}")
    private String BEER_CREATED_KEY;

    @KafkaListener(topics = "beer-topic", groupId = "beer-consumers", containerFactory = "beerCreatedListenerFactory")
    public void handleBeerCreatedEvent(BeerCreatedEvent event, @Header(KafkaHeaders.RECEIVED_KEY) String key) {

        try {

            if (!BEER_CREATED_KEY.equals(key)) {
                log.warn("Received event with unexpected key: {}", key);
                return;
            }

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


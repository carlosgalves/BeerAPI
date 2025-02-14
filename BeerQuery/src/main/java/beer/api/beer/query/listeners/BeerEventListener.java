package beer.api.beer.query.listeners;

import beer.api.beer.query.events.BeerCreatedEvent;
import beer.api.beer.query.events.BeerDeletedEvent;
import beer.api.beer.query.events.BeerUpdatedEvent;
import beer.api.beer.query.model.Beer;
import beer.api.beer.query.repositories.BeerRepository;
import beer.api.beer.query.services.BeerQueryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
@KafkaListener(topics = "beer-topic", groupId = "beer-consumers", containerFactory = "kafkaListenerContainerFactory")
public class BeerEventListener {

    private final BeerRepository beerRepository;

    @Autowired
    public BeerEventListener(BeerRepository beerRepository) {
        this.beerRepository = beerRepository;
    }

    @Value("${spring.kafka.event.key.beer.created}")
    private String BEER_CREATED_KEY;

    @Value("${spring.kafka.event.key.beer.updated}")
    private String BEER_UPDATED_KEY;

    @Value("${spring.kafka.event.key.beer.deleted}")
    private String BEER_DELETED_KEY;

    @KafkaHandler
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

    @KafkaHandler
    public void handleBeerUpdatedEvent(BeerUpdatedEvent event, @Header(KafkaHeaders.RECEIVED_KEY) String key) {

        try {
            if (!BEER_UPDATED_KEY.equals(key)) {
                log.warn("Received event with unexpected key: {}", key);
                return;
            }

            log.info("Received event: {}", event);

            Beer beer = beerRepository.findById(UUID.fromString(event.getId()))
                    .orElseThrow(() -> new RuntimeException("Beer not found"));

            if (event.getName() != null && !event.getName().isEmpty()) {
                beer.setName(event.getName());
            }

            if (event.getBrewery() != null && !event.getBrewery().isEmpty()) {
                beer.setBrewery(event.getBrewery());
            }

            if (event.getType() != null && !event.getType().isEmpty()) {
                beer.setType(event.getType());
            }

            if (event.getImage() != null && !event.getImage().isEmpty()) {
                beer.setImage(event.getImage());
            }

            if (event.getDescription() != null && !event.getDescription().isEmpty()) {
                beer.setDescription(event.getDescription());
            }

            if (!Float.isNaN(event.getAbv())) {
                beer.setAbv(event.getAbv());
            }

            if (event.getCountryIso() != null && !event.getCountryIso().isEmpty()) {
                beer.setCountryIso(event.getCountryIso());
            }

            if (event.getEan() != null && !event.getEan().isEmpty()) {
                beer.setEan(event.getEan());
            }

            if (event.getTags() != null && !event.getTags().isEmpty()) {
                beer.setTags(event.getTags());
            }

            beerRepository.save(beer);
            log.info("Beer updated: {}", beer.getName());

        } catch (Exception e) {
            log.error("Error processing beer update event: {}", e.getMessage(), e);
            throw new RuntimeException("Error processing beer update event" ,e);
        }
    }

    @KafkaHandler
    public void handleBeerDeletedEvent(BeerDeletedEvent event, @Header(KafkaHeaders.RECEIVED_KEY) String key) {

        try {

            if (!BEER_DELETED_KEY.equals(key)) {
                log.warn("Received event with unexpected key: {}", key);
                return;
            }

            log.info("Received event {}", event);

            beerRepository.deleteById(event.getBeerId());
            log.info("Beer deleted from query database: {}", event.getBeerId());
        } catch (Exception e) {
            log.error("Error processing beer delete event: {}", e.getMessage(), e);
            throw new RuntimeException("Error processing beer delete event", e);
        }
    }

}

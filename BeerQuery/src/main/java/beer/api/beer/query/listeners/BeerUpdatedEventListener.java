package beer.api.beer.query.listeners;

import beer.api.beer.query.events.BeerUpdatedEvent;
import beer.api.beer.query.model.Beer;
import beer.api.beer.query.repositories.BeerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class BeerUpdatedEventListener {

    private final BeerRepository beerRepository;

    @Autowired
    public BeerUpdatedEventListener(BeerRepository beerRepository) {
        this.beerRepository = beerRepository;
    }

    @Value("${spring.kafka.event.key.beer.updated}")
    private String BEER_UPDATED_KEY;

    @KafkaListener(topics = "beer-topic", groupId = "beer-consumers", containerFactory = "beerUpdatedListenerFactory")
    public void handleBeerUpdatedEvent(BeerUpdatedEvent event, @Header(KafkaHeaders.RECEIVED_KEY) String key) {

        try {
            if (!BEER_UPDATED_KEY.equals(key)) {
                log.warn("Received event with unexpected key: {}", key);
                return;
            }

            log.info("Received event: {}", event);

            Beer beer = beerRepository.findById(event.getId())
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
}

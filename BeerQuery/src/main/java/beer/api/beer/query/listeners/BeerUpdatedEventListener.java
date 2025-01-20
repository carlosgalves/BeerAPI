package beer.api.beer.query.listeners;

import beer.api.beer.query.events.BeerUpdatedEvent;
import beer.api.beer.query.model.Beer;
import beer.api.beer.query.repositories.BeerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
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


    @KafkaListener(topics = "beer-topic", groupId = "beer-consumers", containerFactory = "kafkaListenerContainerFactory")
    public void handleBeerUpdatedEvent(BeerUpdatedEvent event) {

        try {
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

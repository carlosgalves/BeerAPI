package beer.api.beer.query.listeners;

import beer.api.beer.query.events.BeerDeletedEvent;
import beer.api.beer.query.repositories.BeerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BeerDeletedEventListener {

    private final BeerRepository beerRepository;

    @Autowired
    public BeerDeletedEventListener(BeerRepository beerRepository) {
        this.beerRepository = beerRepository;
    }

    @KafkaListener(topics = "beer-topic", groupId = "beer-consumers", containerFactory = "kafkaListenerContainerFactory")
    public void handleBeerDeletedEvent(BeerDeletedEvent event) {

        try {
            log.info("Received event {}", event);

            beerRepository.deleteById(String.valueOf(event.getBeerId()));
            log.info("Beer deleted from query database: {}", event.getBeerId());
        } catch (Exception e) {
            log.error("Error processing beer delete event: {}", e.getMessage(), e);
            throw new RuntimeException("Error processing beer delete event", e);
        }
    }
}

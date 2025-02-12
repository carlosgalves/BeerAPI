package beer.api.beer.query.listeners;

import beer.api.beer.query.events.BeerDeletedEvent;
import beer.api.beer.query.repositories.BeerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class BeerDeletedEventListener {

    private final BeerRepository beerRepository;

    @Autowired
    public BeerDeletedEventListener(BeerRepository beerRepository) {
        this.beerRepository = beerRepository;
    }

    @Value("${spring.kafka.event.key.beer.deleted}")
    private String BEER_DELETED_KEY;

    @KafkaListener(topics = "beer-topic", groupId = "beer-consumers", containerFactory = "beerDeletedListenerFactory")
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

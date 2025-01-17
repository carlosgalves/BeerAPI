package beer.api.beer.command.services;


import beer.api.beer.command.dto.CreateBeerRequest;
import beer.api.beer.command.events.BeerCreatedEvent;
import beer.api.beer.command.model.Beer;
import beer.api.beer.command.repositories.BeerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BeerCommandServiceImplementation implements BeerCommandService {

    private final BeerRepository beerRepository;
    private final KafkaTemplate<String, BeerCreatedEvent> kafkaTemplate;

    @Autowired
    public BeerCommandServiceImplementation(BeerRepository beerRepository, KafkaTemplate<String, BeerCreatedEvent> kafkaTemplate) {
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
        beer.setOverallRating(request.getOverallRating());
        beer.setAromaRating(request.getAromaRating());
        beer.setTasteRating(request.getTasteRating());
        beer.setAfterTasteRating(request.getAfterTasteRating());

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
                beer.getOverallRating(),
                beer.getAromaRating(),
                beer.getTasteRating(),
                beer.getAfterTasteRating()
        );

        kafkaTemplate.send("beer-topic", event);
    }
}
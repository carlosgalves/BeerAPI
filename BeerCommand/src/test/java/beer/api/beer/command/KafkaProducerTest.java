package beer.api.beer.command;

import beer.api.beer.command.dto.CreateBeerRequest;
import beer.api.beer.command.events.BeerCreatedEvent;
import beer.api.beer.command.services.BeerCommandService;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.kafka.test.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.EnableKafka;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.core.*;
import org.testcontainers.*;


import java.util.HashMap;
import java.util.Map;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestContainers
@ExtendWith(EmbeddedKafkaExtension.class)
@EnableKafka
@EmbeddedKafka(partitions = 1, topics = {"beer-topic"})
public class KafkaProducerTest {

    @Container
    public static KafkaContainer kafkaContainer = new KafkaContainer("confluentinc/cp-kafka:latest");

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private BeerCommandService beerCommandService;

    private KafkaConsumer<String, BeerCreatedEvent> consumer;

    @BeforeEach
    void setup() {
        // Set the bootstrap servers to the Kafka container's address
        System.setProperty("KAFKA_BOOTSTRAP_SERVERS", kafkaContainer.getBootstrapServers());

        // Setup Kafka Consumer configuration
        Map<String, Object> consumerProps = new HashMap<>();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaContainer.getBootstrapServers());
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "test-group");
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        // Create a KafkaConsumer to listen to the "beer-topic"
        consumer = new KafkaConsumer<>(consumerProps);
        consumer.subscribe(List.of("beer-topic"));
    }

    @Test
    void testCreateBeerEventSent() throws InterruptedException {
        // Arrange: Prepare the CreateBeerRequest
        CreateBeerRequest request = new CreateBeerRequest();
        request.setName("Test Beer");
        request.setBrewery("Test Brewery");
        request.setType("IPA");
        request.setImage("image-url");
        request.setDescription("Delicious test beer");
        request.setAbv(5.5f);
        request.setCountryIso("US");
        request.setEan("123456789");
        request.setTags(List.of("tag1", "tag2"));

        // Act: Call the createBeer method which should send the BeerCreatedEvent
        beerCommandService.createBeer(request);

        // Consume the message from the Kafka topic
        ConsumerRecord<String, BeerCreatedEvent> record = KafkaTestUtils.getSingleRecord(consumer, "beer-topic");

        // Assert: Verify the event is sent and the fields match
        BeerCreatedEvent event = record.value();
        assertEquals("Test Beer", event.getName());
        assertEquals("Test Brewery", event.getBrewery());
        assertEquals("IPA", event.getType());
        assertEquals("image-url", event.getImage());
        assertEquals("Delicious test beer", event.getDescription());
        assertEquals(5.5f, event.getAbv());
        assertEquals("US", event.getCountryIso());
        assertEquals("123456789", event.getEan());
        assertEquals(List.of("tag1", "tag2"), event.getTags());
    }
}

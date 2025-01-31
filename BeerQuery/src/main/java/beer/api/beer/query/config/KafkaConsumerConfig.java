package beer.api.beer.query.config;

import beer.api.beer.query.events.BeerCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
@Slf4j
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.type-mappings}")
    private String typeMappings;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    @Value("${spring.kafka.trusted.packages}")
    private String trustedPackages;

    @Value("${spring.kafka.event.key.beer.created}")
    private String BEER_CREATED_KEY;

    @Value("${spring.kafka.event.key.beer.updated}")
    private String BEER_UPDATED_KEY;

    @Value("${spring.kafka.event.key.beer.deleted}")
    private String BEER_DELETED_KEY;

    @Bean
    public Map<String, Object> consumerConfig() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(JsonDeserializer.TYPE_MAPPINGS, typeMappings);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, trustedPackages);

        return props;
    }

    @Bean
    public ConsumerFactory<String, Object> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfig());
    }

    private ConcurrentKafkaListenerContainerFactory<String, Object> createFilteredFactory(String eventKey) {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setRecordFilterStrategy(record -> !eventKey.equals(record.key()));
        return factory;
    }

    @Bean(name = "beerCreatedListenerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, Object> beerCreatedListenerFactory() {
        return createFilteredFactory(BEER_CREATED_KEY);
    }

    @Bean(name = "beerUpdatedListenerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, Object> beerUpdatedListenerFactory() {
        return createFilteredFactory(BEER_UPDATED_KEY);
    }

    @Bean(name = "beerDeletedListenerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, Object> beerDeletedListenerFactory() {
        return createFilteredFactory(BEER_DELETED_KEY);
    }

    /*@Bean(name = "beerCreatedListenerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, Object> beerCreatedListenerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setRecordFilterStrategy(record -> !BEER_CREATED_KEY.equals(record.key()));
        return factory;
    }*/

    /*@Bean(name = "beerUpdatedListenerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, Object> beerUpdatedListenerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setRecordFilterStrategy(record -> !BEER_UPDATED_KEY.equals(record.key()));
        return factory;
    }

    @Bean(name = "beerDeletedListenerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, Object> beerDeletedListenerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setRecordFilterStrategy(record -> !BEER_DELETED_KEY.equals(record.key()));
        return factory;
    }*/

}
package ru.otus.recommendation.controller;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import ru.otus.common.dto.ShopActionDto;
import ru.otus.common.event.ShopEvent;
import ru.otus.recommendation.dao.RecommendationDao;
import ru.otus.recommendation.service.RecommendationService;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@Testcontainers
@SpringBootTest
@Import(value = ShopControllerTest.ShopControllerConfiguration.class)
@TestPropertySource(properties = {"spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.neo4j.Neo4jAutoConfiguration"})
class ShopControllerTest {

    @Container
    private static final KafkaContainer kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.3.0"));

    @DynamicPropertySource
    static void kafkaProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafkaContainer::getBootstrapServers);
    }

    @Autowired
    private ShopController shopController;

    @MockBean
    private RecommendationService recommendationService;

    @MockBean
    private RecommendationDao recommendationDao; // чтобы не поднималась БД

    @Autowired
    private KafkaTemplate<String, ShopEvent> kafkaTemplate;

    @MockBean
    private JwtDecoder jwtDecoder;

    @TestConfiguration
    static class ShopControllerConfiguration {
        @Bean
        public ProducerFactory<String, ShopEvent> producerFactory() {
            Map<String, Object> props = new HashMap<>();
            props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaContainer.getBootstrapServers());
            props.put(ProducerConfig.BATCH_SIZE_CONFIG, "16384");
            props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
            props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, "33554432");
            props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
            props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,  org.springframework.kafka.support.serializer.JsonSerializer.class);
            return new DefaultKafkaProducerFactory<>(props);
        }

        @Bean
        public KafkaTemplate<String, ShopEvent> kafkaTemplate() {
            KafkaTemplate<String, ShopEvent> kafkaTemplate = new KafkaTemplate<>(producerFactory());
            kafkaTemplate.setDefaultTopic("shopTopic");
            return kafkaTemplate;
        }
    }

    @Test
    void shouldHandleShopEvents() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        doAnswer(inv -> {
            latch.countDown();
            return null;
        }).when(recommendationService).handleShopEvent(any());

        ShopEvent shopEvent = new ShopEvent("1234", "100", ShopActionDto.VIEW);
        kafkaTemplate.send("shopTopic", shopEvent);

        boolean isInvoked = latch.await(3, TimeUnit.SECONDS);
        assertThat(isInvoked).isEqualTo(true);

        verify(recommendationService, times(1)).handleShopEvent(argThat(e ->
            e.getClientId().equals(shopEvent.getClientId())
                && e.getProductCode().equals(shopEvent.getProductCode())
                && e.getAction().equals(shopEvent.getAction())
        ));
    }

}
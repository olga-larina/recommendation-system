package ru.otus.shop.service;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.rnorth.ducttape.unreliables.Unreliables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import ru.otus.common.dto.ShopActionDto;
import ru.otus.common.event.ShopEvent;

import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
@Import(value = ShopServiceImplTest.ShopServiceConfiguration.class)
class ShopServiceImplTest {

    @Container
    private static final KafkaContainer kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.3.0"));

    @DynamicPropertySource
    static void kafkaProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafkaContainer::getBootstrapServers);
    }

    @Autowired
    private ShopService shopService;

    @Autowired
    private KafkaConsumer<String, ShopEvent> consumer;

    @MockBean
    private JwtDecoder jwtDecoder;

    @TestConfiguration
    static class ShopServiceConfiguration {
        @Bean
        public KafkaConsumer<String, ShopEvent> consumer() {
            return new KafkaConsumer<>(
                Map.of(
                    ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaContainer.getBootstrapServers(),
                    ConsumerConfig.GROUP_ID_CONFIG, "shopEventId",
                    ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest",
                    ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                    ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class,
                    JsonDeserializer.TYPE_MAPPINGS, "event:ru.otus.common.event.ShopEvent",
                    JsonDeserializer.TRUSTED_PACKAGES, "*"
                )
            );
        }

        @Bean
        public NewTopic topic() {
            return new NewTopic("shopTopic", 1, (short) 1);
        }
    }

    @BeforeEach
    void setUp() {
        consumer.subscribe(Collections.singletonList("shopTopic"));
    }

    @AfterEach
    void tearDown() {
        consumer.unsubscribe();
    }

    @Test
    void shouldProcessShopEvents() throws Exception {
        ShopEvent shopEvent = new ShopEvent("1234", "100", ShopActionDto.VIEW);
        shopService.process(shopEvent.getClientId(), shopEvent.getProductCode(), shopEvent.getAction());

        Unreliables.retryUntilTrue(
            10,
            TimeUnit.SECONDS,
            () -> {
                ConsumerRecords<String, ShopEvent> records = consumer.poll(Duration.ofMillis(100));

                if (records.isEmpty()) {
                    return false;
                }

                assertThat(records)
                    .hasSize(1);

                assertThat(records.iterator().next().value())
                    .usingRecursiveComparison()
                    .isEqualTo(shopEvent);

                return true;
            }
        );
    }

}
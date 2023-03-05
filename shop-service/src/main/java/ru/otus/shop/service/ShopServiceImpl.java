package ru.otus.shop.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.otus.common.dto.ShopActionDto;
import ru.otus.common.event.ShopEvent;

@Service
public class ShopServiceImpl implements ShopService {

    private final KafkaTemplate<String, ShopEvent> kafkaTemplate;

    public ShopServiceImpl(KafkaTemplate<String, ShopEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public boolean process(String clientId, String productCode, ShopActionDto action) {
        kafkaTemplate.send("shopTopic", new ShopEvent(clientId, productCode, action));
        return true;
    }
}

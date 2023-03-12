package ru.otus.recommendation.controller;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.otus.common.event.ShopEvent;
import ru.otus.recommendation.service.RecommendationService;

@Component
public class ShopController {

    private final RecommendationService recommendationService;

    public ShopController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @KafkaListener(topics = "shopTopic")
    public void handleShopEvents(ShopEvent shopEvent) {
        recommendationService.handleShopEvent(shopEvent);
    }
}

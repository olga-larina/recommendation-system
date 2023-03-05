package ru.otus.recommendation.service;

import ru.otus.common.dto.RecommendationDto;
import ru.otus.common.event.ShopEvent;

public interface RecommendationService {

    /**
     * Получить список рекомендаций продуктов для клиента и продукта
     * @param clientId id клиента
     * @param productCode код продукта
     * @param clientToken токен клиента
     * @return рекомендации
     */
    RecommendationDto getRecommendation(String clientId, String productCode, String clientToken);

    /**
     * Учесть событие магазина в рекомендациях
     * @param shopEvent событие
     */
    void handleShopEvent(ShopEvent shopEvent);
}

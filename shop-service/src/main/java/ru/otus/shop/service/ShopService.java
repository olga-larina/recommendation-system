package ru.otus.shop.service;

import ru.otus.common.dto.ShopActionDto;

public interface ShopService {

    /**
     * Выполнить действие над продуктом
     * @param clientId id клиента
     * @param productCode код продукта
     * @param action действие
     * @return результат
     */
    boolean process(String clientId, String productCode, ShopActionDto action);

}

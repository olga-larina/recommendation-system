package ru.otus.recommendation.service;

import ru.otus.common.dto.ProductDto;

import java.util.Collection;
import java.util.List;

public interface ProductService {

    /**
     * Получить список продуктов
     * @param productCodes коды продуктов
     * @param clientToken токен клиента
     * @return найденные продукты
     */
    List<ProductDto> getProducts(Collection<String> productCodes, String clientToken);

}

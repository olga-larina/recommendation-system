package ru.otus.product.service;

import ru.otus.common.dto.ProductDto;

import java.util.List;

public interface ProductService {

    /**
     * Получить все продукты
     * @return продукты
     */
    List<ProductDto> getProducts();

    /**
     * Получить список продуктов
     * @param productCode коды продуктов
     * @return найденные продукты
     */
    List<ProductDto> getProducts(List<String> productCode);

}

package ru.otus.product.service;

import org.springframework.stereotype.Component;
import ru.otus.common.dto.ProductDto;
import ru.otus.product.dao.ProductDao;
import ru.otus.product.domain.Product;
import ru.otus.product.dto.converter.DtoConverter;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductServiceImpl implements ProductService {

    private final ProductDao productDao;
    private final DtoConverter<Product, ProductDto> productConverter;

    public ProductServiceImpl(ProductDao productDao, DtoConverter<Product, ProductDto> productConverter) {
        this.productDao = productDao;
        this.productConverter = productConverter;
    }

    @Override
    public List<ProductDto> getProducts() {
        return productDao.findAll().stream().map(productConverter::toDto).collect(Collectors.toList());
    }

    @Override
    public List<ProductDto> getProducts(List<String> productCode) {
        return productDao.findAllByCodeIn(productCode).stream().map(productConverter::toDto).collect(Collectors.toList());
    }

}

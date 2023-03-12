package ru.otus.product.dto.converter;

import org.springframework.stereotype.Component;
import ru.otus.common.dto.ProductCategoryDto;
import ru.otus.common.dto.ProductDto;
import ru.otus.common.dto.ProductManufacturerDto;
import ru.otus.product.domain.Category;
import ru.otus.product.domain.Manufacturer;
import ru.otus.product.domain.Product;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Component
public class ProductDtoConverter implements DtoConverter<Product, ProductDto> {

    private final DtoConverter<Manufacturer, ProductManufacturerDto> manufacturerConverter;
    private final DtoConverter<Category, ProductCategoryDto> categoryConverter;

    public ProductDtoConverter(DtoConverter<Manufacturer, ProductManufacturerDto> manufacturerConverter,
                               DtoConverter<Category, ProductCategoryDto> categoryConverter) {
        this.manufacturerConverter = manufacturerConverter;
        this.categoryConverter = categoryConverter;
    }

    @Override
    public ProductDto toDto(Product entity) {
        return new ProductDto(
            entity.getId(),
            entity.getTitle(),
            entity.getCode(),
            manufacturerConverter.toDto(entity.getManufacturer()),
            (entity.getCategories() == null) ? new ArrayList<>() : entity.getCategories().stream().map(categoryConverter::toDto).collect(Collectors.toList())
        );
    }

    @Override
    public Product fromDto(ProductDto dto) {
        return new Product(
            dto.getId(),
            dto.getTitle(),
            (dto.getManufacturer() == null) ? null : manufacturerConverter.fromDto(dto.getManufacturer()),
            (dto.getCategories() == null) ? new ArrayList<>() : dto.getCategories().stream().map(categoryConverter::fromDto).collect(Collectors.toList())
        );
    }

}
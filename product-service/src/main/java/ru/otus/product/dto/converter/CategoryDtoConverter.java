package ru.otus.product.dto.converter;

import org.springframework.stereotype.Component;
import ru.otus.common.dto.ProductCategoryDto;
import ru.otus.product.domain.Category;

@Component
public class CategoryDtoConverter implements DtoConverter<Category, ProductCategoryDto> {

    @Override
    public ProductCategoryDto toDto(Category entity) {
        return new ProductCategoryDto(entity.getId(), entity.getTitle());
    }

    @Override
    public Category fromDto(ProductCategoryDto dto) {
        return new Category(dto.getId(), dto.getTitle());
    }
}

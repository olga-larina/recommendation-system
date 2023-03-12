package ru.otus.product.dto.converter;

import org.springframework.stereotype.Component;
import ru.otus.common.dto.ProductManufacturerDto;
import ru.otus.product.domain.Manufacturer;

@Component
public class ManufacturerDtoConverter implements DtoConverter<Manufacturer, ProductManufacturerDto> {

    @Override
    public ProductManufacturerDto toDto(Manufacturer entity) {
        return new ProductManufacturerDto(entity.getId(), entity.getTitle());
    }

    @Override
    public Manufacturer fromDto(ProductManufacturerDto dto) {
        return new Manufacturer(dto.getId(), dto.getTitle());
    }
}

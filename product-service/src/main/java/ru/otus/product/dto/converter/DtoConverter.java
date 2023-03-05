package ru.otus.product.dto.converter;

public interface DtoConverter<E, T> {

    T toDto(E entity);

    E fromDto(T dto);
}

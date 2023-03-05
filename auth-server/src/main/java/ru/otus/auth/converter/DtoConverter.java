package ru.otus.auth.converter;

public interface DtoConverter<E, T> {

    T toDto(E entity);
}

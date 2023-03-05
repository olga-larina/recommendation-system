package ru.otus.product.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.product.domain.Manufacturer;

public interface ManufacturerDao extends MongoRepository<Manufacturer, String> {
}

package ru.otus.product.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.product.domain.Product;

import java.util.Collection;
import java.util.List;

public interface ProductDao extends MongoRepository<Product, String> {

    List<Product> findAllByCodeIn(Collection<String> codes);
}

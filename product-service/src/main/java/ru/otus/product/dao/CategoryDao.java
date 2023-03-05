package ru.otus.product.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.product.domain.Category;

public interface CategoryDao extends MongoRepository<Category, String> {
}

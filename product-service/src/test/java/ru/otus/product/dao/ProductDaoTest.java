package ru.otus.product.dao;

import com.github.cloudyrock.spring.v5.EnableMongock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import ru.otus.product.domain.Product;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.otus.product.mongock.changelog.MongoDatabaseChangelog.PRODUCT_1;
import static ru.otus.product.mongock.changelog.MongoDatabaseChangelog.PRODUCT_2;

@Testcontainers
@DataMongoTest
@EnableMongock
class ProductDaoTest {

    @Container
    private static final MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:6.0.4"));

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    void shouldFindByCodeIn() {
        List<Product> expected = List.of(PRODUCT_1, PRODUCT_2);
        List<String> codes = List.of("100", "200", "500");

        Query query = new Query().addCriteria(Criteria.where("code").in(codes));
        List<Product> products1 = mongoTemplate.find(query, Product.class);
        assertThat(products1).usingRecursiveComparison().ignoringCollectionOrder().isEqualTo(expected);

        List<Product> products2 = productDao.findAllByCodeIn(codes);
        assertThat(products2).usingRecursiveComparison().ignoringCollectionOrder().isEqualTo(expected);
    }
}
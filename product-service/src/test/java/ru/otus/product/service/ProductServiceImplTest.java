package ru.otus.product.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.otus.common.dto.ProductCategoryDto;
import ru.otus.common.dto.ProductDto;
import ru.otus.common.dto.ProductManufacturerDto;
import ru.otus.product.dao.ProductDao;
import ru.otus.product.domain.Category;
import ru.otus.product.domain.Manufacturer;
import ru.otus.product.domain.Product;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
class ProductServiceImplTest {

    @Autowired
    private ProductService productService;

    @MockBean
    private ProductDao productDao;

    // чтобы не поднималась база; используем только сервис
    @Configuration
    @Import({ProductServiceImpl.class})
    @ComponentScan("ru.otus.product.dto.converter")
    static class ProductServiceImplConfiguration {
    }

    @Test
    void shouldGetAllProducts() {
        List<Product> products = List.of(
            new Product("1", "test1", "100", new Manufacturer("11", "Test1 Test1"), List.of(new Category("12", "Test cat1"))),
            new Product("2", "test2", "200", new Manufacturer("21", "Test2 Test2"), List.of(new Category("22", "Test cat2"))),
            new Product("3", "test3", "300", new Manufacturer("31", "Test3 Test3"), List.of(new Category("32", "Test cat3")))
        );
        List<ProductDto> productDtos = List.of(
            new ProductDto("1", "test1", "100", new ProductManufacturerDto("11", "Test1 Test1"), List.of(new ProductCategoryDto("12", "Test cat1"))),
            new ProductDto("2", "test2", "200", new ProductManufacturerDto("21", "Test2 Test2"), List.of(new ProductCategoryDto("22", "Test cat2"))),
            new ProductDto("3", "test3", "300", new ProductManufacturerDto("31", "Test3 Test3"), List.of(new ProductCategoryDto("32", "Test cat3")))
        );
        when(productDao.findAll()).thenReturn(products);

        List<ProductDto> actual = productService.getProducts();
        assertThat(actual).usingRecursiveComparison().isEqualTo(productDtos);
    }

    @Test
    void shouldGetProductsByCodes() {
        List<Product> products = List.of(
            new Product("1", "test1", "100", new Manufacturer("11", "Test1 Test1"), List.of(new Category("12", "Test cat1"))),
            new Product("2", "test2", "200", new Manufacturer("21", "Test2 Test2"), List.of(new Category("22", "Test cat2"))),
            new Product("3", "test3", "300", new Manufacturer("31", "Test3 Test3"), List.of(new Category("32", "Test cat3")))
        );
        List<ProductDto> productDtos = List.of(
            new ProductDto("1", "test1", "100", new ProductManufacturerDto("11", "Test1 Test1"), List.of(new ProductCategoryDto("12", "Test cat1"))),
            new ProductDto("2", "test2", "200", new ProductManufacturerDto("21", "Test2 Test2"), List.of(new ProductCategoryDto("22", "Test cat2"))),
            new ProductDto("3", "test3", "300", new ProductManufacturerDto("31", "Test3 Test3"), List.of(new ProductCategoryDto("32", "Test cat3")))
        );
        List<String> productCodes = List.of("100", "200", "300");
        when(productDao.findAllByCodeIn(productCodes)).thenReturn(products);

        List<ProductDto> actual = productService.getProducts(productCodes);
        assertThat(actual).usingRecursiveComparison().isEqualTo(productDtos);
    }
}
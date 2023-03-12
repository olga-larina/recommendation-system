package ru.otus.recommendation.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;
import ru.otus.common.dto.ProductCategoryDto;
import ru.otus.common.dto.ProductDto;
import ru.otus.common.dto.ProductManufacturerDto;
import ru.otus.recommendation.config.ApiConfig;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ProductServiceImplTest {

    private static MockWebServer mockBackEnd;

    @BeforeAll
    static void setUp() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start();
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockBackEnd.shutdown();
    }

    @Test
    void shouldGetProducts() throws Exception {
        ProductService productService = new ProductServiceImpl(WebClient.builder(),
            new ApiConfig().setProductUri(String.format("http://localhost:%s", mockBackEnd.getPort())));

        ObjectMapper mapper = new ObjectMapper();

        List<ProductDto> expected = List.of(
            new ProductDto("1", "test1", "100", new ProductManufacturerDto("11", "Test1 Test1"), List.of(new ProductCategoryDto("12", "Test cat1"))),
            new ProductDto("2", "test2", "200", new ProductManufacturerDto("21", "Test2 Test2"), List.of(new ProductCategoryDto("22", "Test cat2"))),
            new ProductDto("3", "test3", "300", new ProductManufacturerDto("31", "Test3 Test3"), List.of(new ProductCategoryDto("32", "Test cat3")))
        );
        mockBackEnd.enqueue(new MockResponse()
            .setBody(mapper.writeValueAsString(expected))
            .addHeader("Content-Type", "application/json"));

        List<ProductDto> actual = productService.getProducts(List.of("111", "222"), "myToken");
        assertThat(actual.size()).isEqualTo(expected.size());
        for (int i = 0; i < actual.size(); i++) {
            assertThat(actual.get(i)).usingRecursiveComparison().isEqualTo(expected.get(i));
        }

        RecordedRequest request = mockBackEnd.takeRequest();
        assertThat(request.getMethod()).isEqualTo("GET");
        assertThat(request.getPath()).isEqualTo("/?productCode=111&productCode=222");
        assertThat(request.getHeader(HttpHeaders.AUTHORIZATION)).isEqualTo("myToken");
    }

}
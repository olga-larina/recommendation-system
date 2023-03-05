package ru.otus.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.common.dto.ProductCategoryDto;
import ru.otus.common.dto.ProductDto;
import ru.otus.common.dto.ProductManufacturerDto;
import ru.otus.product.config.SecurityConfig;
import ru.otus.product.dao.ProductDao;
import ru.otus.product.service.ProductService;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
@Import(SecurityConfig.class)
class ProductControllerTest {

    @MockBean
    private ProductService productService;

    @MockBean
    private ProductDao productDao;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private JwtDecoder jwtDecoder;

    // чтобы не поднималась база
    @Configuration
    @Import(ProductController.class)
    static class ProductControllerConfiguration {
    }

    @Test
    void shouldGetProductsWithParamsIfAuthorized() throws Exception {
        Jwt jwt = Jwt.withTokenValue("token")
            .header("alg", "none")
            .claim("client_id", "1234")
            .claim("user_authorities", "ROLE_PRODUCT_USER")
            .issuer("http://localhost:9000")
            .build();
        when(jwtDecoder.decode(anyString())).thenReturn(jwt);

        String token = "Bearer " + jwt.getTokenValue();

        List<ProductDto> products = List.of(
            new ProductDto("1", "test1", "100", new ProductManufacturerDto("11", "Test1 Test1"), List.of(new ProductCategoryDto("12", "Test cat1"))),
            new ProductDto("2", "test2", "200", new ProductManufacturerDto("21", "Test2 Test2"), List.of(new ProductCategoryDto("22", "Test cat2"))),
            new ProductDto("3", "test3", "300", new ProductManufacturerDto("31", "Test3 Test3"), List.of(new ProductCategoryDto("32", "Test cat3")))
        );
        List<String> productCodes = List.of("123", "456", "789");
        when(productService.getProducts(eq(productCodes))).thenReturn(products);

        mvc.perform(get("/product")
                .with(jwt().jwt(jwt))
                .param("productCode", productCodes.toArray(String[]::new))
                .header(HttpHeaders.AUTHORIZATION, token))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().json(mapper.writeValueAsString(products)));

        verify(productService, times(0)).getProducts();
        verify(productService, times(1)).getProducts(eq(productCodes));
    }

    @Test
    void shouldGetAllProductsIfAuthorized() throws Exception {
        Jwt jwt = Jwt.withTokenValue("token")
            .header("alg", "none")
            .claim("client_id", "1234")
            .claim("user_authorities", "ROLE_PRODUCT_USER")
            .issuer("http://localhost:9000")
            .build();
        when(jwtDecoder.decode(anyString())).thenReturn(jwt);

        String token = "Bearer " + jwt.getTokenValue();

        List<ProductDto> products = List.of(
            new ProductDto("1", "test1", "100", new ProductManufacturerDto("11", "Test1 Test1"), List.of(new ProductCategoryDto("12", "Test cat1"))),
            new ProductDto("2", "test2", "200", new ProductManufacturerDto("21", "Test2 Test2"), List.of(new ProductCategoryDto("22", "Test cat2"))),
            new ProductDto("3", "test3", "300", new ProductManufacturerDto("31", "Test3 Test3"), List.of(new ProductCategoryDto("32", "Test cat3")))
        );
        when(productService.getProducts()).thenReturn(products);

        mvc.perform(get("/product")
                .with(jwt().jwt(jwt))
                .header(HttpHeaders.AUTHORIZATION, token))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().json(mapper.writeValueAsString(products)));

        verify(productService, times(1)).getProducts();
        verify(productService, times(0)).getProducts(any());
    }

    @Test
    void shouldThrowGetProductsIfNotAuthorized() throws Exception {
        Jwt jwt = Jwt.withTokenValue("token")
            .header("alg", "none")
            .claim("client_id", "1234")
            .issuer("http://localhost:9000")
            .build();
        when(jwtDecoder.decode(anyString())).thenReturn(jwt);

        String token = "Bearer " + jwt.getTokenValue();

        mvc.perform(get("/product")
                .with(jwt().jwt(jwt))
                .header(HttpHeaders.AUTHORIZATION, token))
            .andDo(print())
            .andExpect(status().is4xxClientError());
    }
}
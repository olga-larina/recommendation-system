package ru.otus.recommendation.service;

import kotlin.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.otus.common.dto.*;
import ru.otus.common.event.ShopEvent;
import ru.otus.recommendation.config.AppConfig;
import ru.otus.recommendation.dao.RecommendationDao;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
class RecommendationServiceImplTest {

    @Autowired
    private RecommendationService recommendationService;

    @MockBean
    private RecommendationDao recommendationDao;

    @MockBean
    private ProductService productService;

    @MockBean
    private AppConfig appConfig;

    // чтобы не поднималась база; используем только сервис
    @Configuration
    @Import({RecommendationServiceImpl.class})
    static class RecommendationServiceImplConfiguration {
    }

    @BeforeEach
    void setUp() {
        when(appConfig.getMinScoreForProduct()).thenReturn(50L);
        when(appConfig.getMinScoreForOtherProducts()).thenReturn(60L);
        when(appConfig.getViewScore()).thenReturn(10L);
        when(appConfig.getFavoritesScore()).thenReturn(40L);
        when(appConfig.getCartScore()).thenReturn(100L);
    }

    @Test
    void shouldGetRecommendation() {
        String clientId = "CLIENT_ID";
        String productCode = "100";
        String clientToken = "myToken";
        Set<String> productCodes = Set.of("123", "456", "789");
        List<ProductDto> products = List.of(
            new ProductDto("1", "test1", "100", new ProductManufacturerDto("11", "Test1 Test1"), List.of(new ProductCategoryDto("12", "Test cat1"))),
            new ProductDto("2", "test2", "200", new ProductManufacturerDto("21", "Test2 Test2"), List.of(new ProductCategoryDto("22", "Test cat2"))),
            new ProductDto("3", "test3", "300", new ProductManufacturerDto("31", "Test3 Test3"), List.of(new ProductCategoryDto("32", "Test cat3")))
        );
        when(recommendationDao.findMatchesByProductPersonScore(eq(productCode), eq(clientId), eq(50L), eq(60L))).thenReturn(productCodes);
        when(productService.getProducts(eq(productCodes), eq(clientToken))).thenReturn(products);

        RecommendationDto recommendation = recommendationService.getRecommendation(clientId, productCode, clientToken);
        assertThat(recommendation.getClientId()).isEqualTo(clientId);
        assertThat(recommendation.getProduct()).isEqualTo(products);
    }

    @Test
    void shouldHandleShopEvent() {
        for (Pair<ShopActionDto, Long> pair : List.of(
            new Pair<>(ShopActionDto.VIEW, appConfig.getViewScore()),
            new Pair<>(ShopActionDto.FAVORITES, appConfig.getFavoritesScore()),
            new Pair<>(ShopActionDto.CART, appConfig.getCartScore())
        )) {
            ShopEvent shopEvent = new ShopEvent("CLIENT_ID", "100", pair.getFirst());
            recommendationService.handleShopEvent(shopEvent);
            verify(recommendationDao, times(1)).matchProductAndPersonWithScore(eq("100"), eq("CLIENT_ID"), eq(pair.getSecond()));
        }
    }
}

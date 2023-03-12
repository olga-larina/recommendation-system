package ru.otus.recommendation.service;

import org.springframework.stereotype.Component;
import ru.otus.common.dto.ProductDto;
import ru.otus.common.dto.RecommendationDto;
import ru.otus.common.dto.ShopActionDto;
import ru.otus.common.event.ShopEvent;
import ru.otus.recommendation.config.AppConfig;
import ru.otus.recommendation.dao.RecommendationDao;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Component
public class RecommendationServiceImpl implements RecommendationService {

    private final ProductService productService;
    private final RecommendationDao recommendationDao;
    private final AppConfig appConfig;

    public RecommendationServiceImpl(ProductService productService, RecommendationDao recommendationDao, AppConfig appConfig) {
        this.productService = productService;
        this.recommendationDao = recommendationDao;
        this.appConfig = appConfig;
    }

    @Override
    public RecommendationDto getRecommendation(String clientId, String productCode, String clientToken) {
        Collection<String> recommendedProducts = recommendationDao.findMatchesByProductPersonScore(productCode, clientId, appConfig.getMinScoreForProduct(), appConfig.getMinScoreForOtherProducts());
        if (recommendedProducts == null || recommendedProducts.isEmpty()) {
            return new RecommendationDto(clientId, Collections.emptyList());
        }
        List<ProductDto> products = productService.getProducts(recommendedProducts, clientToken);
        return new RecommendationDto(clientId, products);
    }

    @Override
    public void handleShopEvent(ShopEvent shopEvent) {
        long score;
        if (shopEvent.getAction() == ShopActionDto.VIEW) {
            score = appConfig.getViewScore();
        } else if (shopEvent.getAction() == ShopActionDto.FAVORITES) {
            score = appConfig.getFavoritesScore();
        } else if (shopEvent.getAction() == ShopActionDto.CART) {
            score = appConfig.getCartScore();
        } else {
            score = 0L;
        }
        if (score != 0L) {
            recommendationDao.matchProductAndPersonWithScore(shopEvent.getProductCode(), shopEvent.getClientId(), score);
        }
    }
}

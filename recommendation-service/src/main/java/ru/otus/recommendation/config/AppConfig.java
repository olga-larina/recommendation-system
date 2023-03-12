package ru.otus.recommendation.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "otus.recommendation.app")
@Configuration
public class AppConfig {

    private long minScoreForProduct;
    private long minScoreForOtherProducts;
    private long viewScore;
    private long favoritesScore;
    private long cartScore;

    public long getMinScoreForProduct() {
        return minScoreForProduct;
    }

    public AppConfig setMinScoreForProduct(long minScoreForProduct) {
        this.minScoreForProduct = minScoreForProduct;
        return this;
    }

    public long getMinScoreForOtherProducts() {
        return minScoreForOtherProducts;
    }

    public AppConfig setMinScoreForOtherProducts(long minScoreForOtherProducts) {
        this.minScoreForOtherProducts = minScoreForOtherProducts;
        return this;
    }

    public long getViewScore() {
        return viewScore;
    }

    public AppConfig setViewScore(long viewScore) {
        this.viewScore = viewScore;
        return this;
    }

    public long getFavoritesScore() {
        return favoritesScore;
    }

    public AppConfig setFavoritesScore(long favoritesScore) {
        this.favoritesScore = favoritesScore;
        return this;
    }

    public long getCartScore() {
        return cartScore;
    }

    public AppConfig setCartScore(long cartScore) {
        this.cartScore = cartScore;
        return this;
    }
}

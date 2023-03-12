package ru.otus.client.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "otus.client.api")
@Configuration
public class ApiConfig {

    private String recommendationUri;

    private String shopUri;

    private String productUri;

    public String getRecommendationUri() {
        return recommendationUri;
    }

    public ApiConfig setRecommendationUri(String recommendationUri) {
        this.recommendationUri = recommendationUri;
        return this;
    }

    public String getShopUri() {
        return shopUri;
    }

    public ApiConfig setShopUri(String shopUri) {
        this.shopUri = shopUri;
        return this;
    }

    public String getProductUri() {
        return productUri;
    }

    public ApiConfig setProductUri(String productUri) {
        this.productUri = productUri;
        return this;
    }
}

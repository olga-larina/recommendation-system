package ru.otus.recommendation.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "otus.recommendation.api")
@Configuration
public class ApiConfig {

    private String productUri;

    public String getProductUri() {
        return productUri;
    }

    public ApiConfig setProductUri(String productUri) {
        this.productUri = productUri;
        return this;
    }
}

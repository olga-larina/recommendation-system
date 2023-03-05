package ru.otus.recommendation.service;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.otus.common.dto.ProductDto;
import ru.otus.recommendation.config.ApiConfig;

import java.util.Collection;
import java.util.List;

@Component
public class ProductServiceImpl implements ProductService {

    private final WebClient.Builder webClientBuilder;
    private final String productUri;

    public ProductServiceImpl(WebClient.Builder webClientBuilder, ApiConfig apiConfig) {
        this.webClientBuilder = webClientBuilder;
        this.productUri = apiConfig.getProductUri();
    }

    @Override
    public List<ProductDto> getProducts(Collection<String> productCodes, String clientToken) {
        return webClientBuilder
            .build()
            .get()
            .uri(productUri, uriBuilder -> uriBuilder.queryParam("productCode", productCodes).build())
            .header(HttpHeaders.AUTHORIZATION, clientToken)
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<List<ProductDto>>() {})
            .block();
    }

}

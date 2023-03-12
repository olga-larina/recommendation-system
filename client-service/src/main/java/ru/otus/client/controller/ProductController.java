package ru.otus.client.controller;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.otus.client.config.ApiConfig;
import ru.otus.common.dto.ProductDto;

import java.util.List;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient;

@RestController
public class ProductController {

    private final WebClient webClient;
    private final String productUri;

    public ProductController(WebClient webClient, ApiConfig apiConfig) {
        this.webClient = webClient;
        this.productUri = apiConfig.getProductUri();
    }

    @GetMapping("/product")
    public ResponseEntity<List<ProductDto>> product(@RegisteredOAuth2AuthorizedClient("recommendation-client") OAuth2AuthorizedClient authorizedClient) {
        Mono<List<ProductDto>> products = this.webClient
            .get()
            .uri(productUri)
            .attributes(oauth2AuthorizedClient(authorizedClient))
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<>() {});
        return ResponseEntity.ok(products.block());
    }
}

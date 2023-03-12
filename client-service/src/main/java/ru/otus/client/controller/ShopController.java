package ru.otus.client.controller;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.otus.client.config.ApiConfig;
import ru.otus.common.dto.ShopActionDto;
import ru.otus.common.dto.ShopRequestDto;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient;

@RestController
public class ShopController {

    private final WebClient webClient;
    private final String shopUri;

    public ShopController(WebClient webClient, ApiConfig apiConfig) {
        this.webClient = webClient;
        this.shopUri = apiConfig.getShopUri();
    }

    @GetMapping("/view/{productCode}")
    public ResponseEntity<Boolean> view(@RegisteredOAuth2AuthorizedClient("recommendation-client") OAuth2AuthorizedClient authorizedClient,
                                        @PathVariable String productCode) {
        return ResponseEntity.ok(processShopQuery(authorizedClient, productCode, ShopActionDto.VIEW));
    }

    @GetMapping("/favorites/{productCode}")
    public ResponseEntity<Boolean> favorites(@RegisteredOAuth2AuthorizedClient("recommendation-client") OAuth2AuthorizedClient authorizedClient,
                                             @PathVariable String productCode) {
        return ResponseEntity.ok(processShopQuery(authorizedClient, productCode, ShopActionDto.FAVORITES));
    }

    @GetMapping("/cart/{productCode}")
    public ResponseEntity<Boolean> cart(@RegisteredOAuth2AuthorizedClient("recommendation-client") OAuth2AuthorizedClient authorizedClient,
                                        @PathVariable String productCode) {
        return ResponseEntity.ok(processShopQuery(authorizedClient, productCode, ShopActionDto.CART));
    }

    private boolean processShopQuery(OAuth2AuthorizedClient authorizedClient, String productCode, ShopActionDto action) {
        Mono<Boolean> result = this.webClient
            .post()
            .uri(shopUri)
            .attributes(oauth2AuthorizedClient(authorizedClient))
            .bodyValue(new ShopRequestDto(action, productCode))
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<>() {});
        return result.block();
    }
}

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
import ru.otus.common.dto.RecommendationDto;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient;

@RestController
public class RecommendationController {

    private final WebClient webClient;
    private final String recommendationUri;

    public RecommendationController(WebClient webClient, ApiConfig apiConfig) {
        this.webClient = webClient;
        this.recommendationUri = apiConfig.getRecommendationUri();
    }

    @GetMapping(value = "/recommendation/{productCode}")
    public ResponseEntity<RecommendationDto> recommendation(@RegisteredOAuth2AuthorizedClient("recommendation-client") OAuth2AuthorizedClient authorizedClient,
                                                            @PathVariable String productCode) {

        Mono<RecommendationDto> recommendation = this.webClient
            .get()
            .uri(recommendationUri, uriBuilder -> uriBuilder.queryParam("productCode", productCode).build())
            .attributes(oauth2AuthorizedClient(authorizedClient))
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<>() {});
        return ResponseEntity.ok(recommendation.block());
    }

}

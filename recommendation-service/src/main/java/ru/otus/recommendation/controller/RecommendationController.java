package ru.otus.recommendation.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.common.dto.RecommendationDto;
import ru.otus.recommendation.service.RecommendationService;

@RestController
public class RecommendationController {

    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping("/recommendation")
    public ResponseEntity<RecommendationDto> getRecommendation(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                               @RequestParam String productCode) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String clientId = null;
        Jwt jwtToken = null;
        if (authentication != null && authentication.getCredentials() instanceof Jwt) {
            jwtToken = (Jwt) authentication.getCredentials();
            clientId = jwtToken.getClaim("client_id");
        }
        if (clientId == null || clientId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(recommendationService.getRecommendation(clientId, productCode, token));
    }
}
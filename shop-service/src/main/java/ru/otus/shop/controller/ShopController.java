package ru.otus.shop.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.common.dto.ShopRequestDto;
import ru.otus.shop.service.ShopService;

@RestController
public class ShopController {

    private final ShopService shopService;

    public ShopController(ShopService shopService) {
        this.shopService = shopService;
    }

    @PostMapping("/shop")
    public ResponseEntity<Boolean> shop(@RequestBody ShopRequestDto request) {
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
        return ResponseEntity.ok(shopService.process(clientId, request.getProductCode(), request.getAction()));
    }
}
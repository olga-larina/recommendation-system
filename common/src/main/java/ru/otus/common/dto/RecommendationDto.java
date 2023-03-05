package ru.otus.common.dto;

import java.util.List;

public class RecommendationDto {

    private String clientId;
    private List<ProductDto> products;

    public RecommendationDto() {
    }

    public RecommendationDto(String clientId, List<ProductDto> products) {
        this.clientId = clientId;
        this.products = products;
    }

    public String getClientId() {
        return clientId;
    }

    public RecommendationDto setClientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    public List<ProductDto> getProduct() {
        return products;
    }

    public RecommendationDto setProduct(List<ProductDto> products) {
        this.products = products;
        return this;
    }
}

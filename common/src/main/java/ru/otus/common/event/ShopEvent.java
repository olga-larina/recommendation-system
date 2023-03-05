package ru.otus.common.event;

import ru.otus.common.dto.ShopActionDto;

public class ShopEvent {

    private String clientId;
    private String productCode;
    private ShopActionDto action;

    public ShopEvent() {
    }

    public ShopEvent(String clientId, String productCode, ShopActionDto action) {
        this.clientId = clientId;
        this.productCode = productCode;
        this.action = action;
    }

    public String getClientId() {
        return clientId;
    }

    public String getProductCode() {
        return productCode;
    }

    public ShopActionDto getAction() {
        return action;
    }

    public ShopEvent setClientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    public ShopEvent setProductCode(String productCode) {
        this.productCode = productCode;
        return this;
    }

    public ShopEvent setAction(ShopActionDto action) {
        this.action = action;
        return this;
    }

    @Override
    public String toString() {
        return "ShopEvent{" +
            "clientId='" + clientId + '\'' +
            ", productCode='" + productCode + '\'' +
            ", action=" + action +
            '}';
    }
}

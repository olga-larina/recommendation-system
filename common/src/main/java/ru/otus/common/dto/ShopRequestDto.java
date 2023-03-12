package ru.otus.common.dto;

public class ShopRequestDto {

    private ShopActionDto action;
    private String productCode;

    public ShopRequestDto() {
    }

    public ShopRequestDto(ShopActionDto action, String productCode) {
        this.action = action;
        this.productCode = productCode;
    }

    public ShopActionDto getAction() {
        return action;
    }

    public ShopRequestDto setAction(ShopActionDto action) {
        this.action = action;
        return this;
    }

    public String getProductCode() {
        return productCode;
    }

    public ShopRequestDto setProductCode(String productCode) {
        this.productCode = productCode;
        return this;
    }

    @Override
    public String toString() {
        return "ShopRequestDto{" +
            "action=" + action +
            ", productCode='" + productCode + '\'' +
            '}';
    }
}

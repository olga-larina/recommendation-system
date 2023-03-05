package ru.otus.auth.domain;

public enum Role {
    RECOMMENDATION_USER, RECOMMENDATION_ADMIN,
    PRODUCT_USER, PRODUCT_ADMIN,
    SHOP_USER, SHOP_ADMIN;

    public String getAuthority() {
        return "ROLE_" + name();
    }
}

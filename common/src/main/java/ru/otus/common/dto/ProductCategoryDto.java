package ru.otus.common.dto;

public class ProductCategoryDto {

    private String id;
    private String title;

    public ProductCategoryDto() {
    }

    public ProductCategoryDto(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public ProductCategoryDto setId(String id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public ProductCategoryDto setTitle(String title) {
        this.title = title;
        return this;
    }
}

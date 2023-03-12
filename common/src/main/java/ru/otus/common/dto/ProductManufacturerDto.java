package ru.otus.common.dto;

public class ProductManufacturerDto {

    private String id;
    private String title;

    public ProductManufacturerDto() {
    }

    public ProductManufacturerDto(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public ProductManufacturerDto setId(String id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public ProductManufacturerDto setTitle(String title) {
        this.title = title;
        return this;
    }
}

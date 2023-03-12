package ru.otus.common.dto;

import java.util.List;

public class ProductDto {

    private String id;
    private String title;
    private String code;
    private ProductManufacturerDto manufacturer;
    private List<ProductCategoryDto> categories;

    public ProductDto() {
    }

    public ProductDto(String id, String title, String code, ProductManufacturerDto manufacturer, List<ProductCategoryDto> categories) {
        this.id = id;
        this.title = title;
        this.code = code;
        this.categories = categories;
        this.manufacturer = manufacturer;
    }

    public String getId() {
        return id;
    }

    public ProductDto setId(String id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public ProductDto setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getCode() {
        return code;
    }

    public ProductDto setCode(String code) {
        this.code = code;
        return this;
    }

    public List<ProductCategoryDto> getCategories() {
        return categories;
    }

    public ProductDto setCategories(List<ProductCategoryDto> categories) {
        this.categories = categories;
        return this;
    }

    public ProductManufacturerDto getManufacturer() {
        return manufacturer;
    }

    public ProductDto setManufacturer(ProductManufacturerDto manufacturer) {
        this.manufacturer = manufacturer;
        return this;
    }
}

package ru.otus.product.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "product")
public class Product {

    @Id
    private String id;

    @NotNull
    @NotBlank
    @Field(name = "title")
    private String title;

    @NotNull
    @NotBlank
    @Field(name = "code")
    private String code;

    @NotNull
    @Field(name = "categories")
    @DBRef
    private List<Category> categories = new ArrayList<>();

    @NotNull
    @Field(name = "manufacturer")
    @DBRef
    private Manufacturer manufacturer;

    public Product() {
    }

    public Product(String title, String code, Manufacturer manufacturer, List<Category> categories) {
        this(null, title, code, manufacturer, categories);
    }

    public Product(String id, String title, String code, Manufacturer manufacturer, List<Category> categories) {
        this.id = id;
        this.title = title;
        this.code = code;
        this.manufacturer = manufacturer;
        this.categories = categories;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Product setId(String id) {
        this.id = id;
        return this;
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public Product setTitle(String title) {
        this.title = title;
        return this;
    }

    public Product setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
        return this;
    }

    public Product setCategories(List<Category> categories) {
        this.categories = categories;
        return this;
    }

    public String getCode() {
        return code;
    }

    public Product setCode(String code) {
        this.code = code;
        return this;
    }
}

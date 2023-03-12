package ru.otus.product.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@Document(collection = "manufacturer")
public class Manufacturer {

    @Id
    private String id;

    @NotNull
    @NotBlank
    @Field(name = "title")
    private String title;

    public Manufacturer() {
    }

    public Manufacturer(String title) {
        this(null, title);
    }

    public Manufacturer(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public Manufacturer setId(String id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Manufacturer setTitle(String title) {
        this.title = title;
        return this;
    }
}

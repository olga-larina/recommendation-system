package ru.otus.recommendation.domain;

import org.springframework.data.neo4j.core.schema.*;

@RelationshipProperties
public class Likes {

    @Id
    @GeneratedValue
    private Long id;

    @TargetNode
    private Product product;

    @Property("score")
    private Long score;

    public Likes() {
    }

    public Long getId() {
        return id;
    }

    public Likes setId(Long id) {
        this.id = id;
        return this;
    }

    public Product getProduct() {
        return product;
    }

    public Likes setProduct(Product product) {
        this.product = product;
        return this;
    }

    public Long getScore() {
        return score;
    }

    public Likes setScore(Long score) {
        this.score = score;
        return this;
    }
}

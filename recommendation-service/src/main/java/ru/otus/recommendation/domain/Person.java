package ru.otus.recommendation.domain;

import org.springframework.data.neo4j.core.schema.*;

import java.util.ArrayList;
import java.util.List;

@Node("Person")
public class Person {

    @Id
    @GeneratedValue
    private Long id;

    @Property("clientId")
    private String clientId;

    @Relationship(type = "LIKES", direction = Relationship.Direction.OUTGOING)
    private List<Likes> likes = new ArrayList<>();

    public Person() {
    }

    public Person(String clientId) {
        this.clientId = clientId;
    }

    public Long getId() {
        return id;
    }

    public Person setId(Long id) {
        this.id = id;
        return this;
    }

    public String getClientId() {
        return clientId;
    }

    public Person setClientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    public List<Likes> getLikes() {
        return likes;
    }

    public Person setLikes(List<Likes> likes) {
        this.likes = likes;
        return this;
    }
}

package ru.otus.auth.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "user_claim")
public class UserClaim {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "client_id", nullable = false, unique = true)
    private String clientId;

    public UserClaim() {
    }

    public Long getId() {
        return id;
    }

    public UserClaim setId(Long id) {
        this.id = id;
        return this;
    }

    public String getClientId() {
        return clientId;
    }

    public UserClaim setClientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public UserClaim setUsername(String username) {
        this.username = username;
        return this;
    }

}

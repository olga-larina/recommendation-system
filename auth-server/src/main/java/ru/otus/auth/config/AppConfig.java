package ru.otus.auth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@ConfigurationProperties(prefix = "otus.auth")
@Configuration
public class AppConfig {

    private String issuer;
    private String clientId;
    private String clientSecret;
    private List<String> redirectUri;

    public String getIssuer() {
        return issuer;
    }

    public AppConfig setIssuer(String issuer) {
        this.issuer = issuer;
        return this;
    }

    public String getClientId() {
        return clientId;
    }

    public AppConfig setClientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public AppConfig setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
        return this;
    }

    public List<String> getRedirectUri() {
        return redirectUri;
    }

    public AppConfig setRedirectUri(List<String> redirectUri) {
        this.redirectUri = redirectUri;
        return this;
    }
}

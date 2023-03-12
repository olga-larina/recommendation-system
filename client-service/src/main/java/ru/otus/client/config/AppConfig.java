package ru.otus.client.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "otus.client.app")
@Configuration
public class AppConfig {

    private String loginPage;
    private String defaultOauth2Registration;

    public String getLoginPage() {
        return loginPage;
    }

    public AppConfig setLoginPage(String loginPage) {
        this.loginPage = loginPage;
        return this;
    }

    public String getDefaultOauth2Registration() {
        return defaultOauth2Registration;
    }

    public AppConfig setDefaultOauth2Registration(String defaultOauth2Registration) {
        this.defaultOauth2Registration = defaultOauth2Registration;
        return this;
    }
}

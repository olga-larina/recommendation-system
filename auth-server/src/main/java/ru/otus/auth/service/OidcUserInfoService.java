package ru.otus.auth.service;

import org.springframework.security.oauth2.core.oidc.OidcUserInfo;

public interface OidcUserInfoService {

    OidcUserInfo loadUserByUsername(String username);
}

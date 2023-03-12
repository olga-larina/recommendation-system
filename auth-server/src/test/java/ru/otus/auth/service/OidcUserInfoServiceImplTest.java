package ru.otus.auth.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import ru.otus.auth.dao.UserClaimDao;
import ru.otus.auth.domain.UserClaim;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@SpringBootTest
class OidcUserInfoServiceImplTest {

    @Autowired
    private OidcUserInfoService oidcUserInfoService;

    @MockBean
    private UserClaimDao userClaimDao;

    // чтобы не поднималась база; используем только сервис и конвертеры
    @Configuration
    @Import(OidcUserInfoServiceImpl.class)
    @ComponentScan("ru.otus.auth.converter")
    static class OidcUserInfoServiceImplConfiguration {
    }

    @Test
    void shouldLoadOidcByUsername() {
        UserClaim userClaim = new UserClaim().setUsername("testUser").setClientId("testClientId");
        when(userClaimDao.findByUsername(userClaim.getUsername())).thenReturn(Optional.of(userClaim));

        OidcUserInfo expected = OidcUserInfo.builder()
            .name(userClaim.getUsername())
            .claim("client_id", userClaim.getClientId())
            .build();

        OidcUserInfo actual = oidcUserInfoService.loadUserByUsername(userClaim.getUsername());
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void shouldThrowLoadingOidcIfNotFound() {
        assertThatThrownBy(() -> {
            oidcUserInfoService.loadUserByUsername("AAAA");
        }).isInstanceOf(UsernameNotFoundException.class).hasMessage("User not found");
    }
}
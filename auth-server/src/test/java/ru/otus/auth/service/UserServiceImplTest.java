package ru.otus.auth.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.otus.auth.dao.UserDao;
import ru.otus.auth.domain.Role;
import ru.otus.auth.domain.User;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserDao userDao;

    // чтобы не поднималась база; используем только сервис и конвертеры
    @Configuration
    @Import(UserServiceImpl.class)
    @ComponentScan("ru.otus.auth.converter")
    static class UserServiceImplConfiguration {
    }

    @Test
    void shouldLoadUserByUsername() {
        User user = new User().setUsername("testUser").setPassword("pwd").setAuthorities(Set.of(Role.PRODUCT_USER, Role.RECOMMENDATION_USER, Role.SHOP_ADMIN));
        when(userDao.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        UserDetails expected = new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
            true, true, true, true,
            Set.of(new SimpleGrantedAuthority("ROLE_PRODUCT_USER"), new SimpleGrantedAuthority("ROLE_RECOMMENDATION_USER"), new SimpleGrantedAuthority("ROLE_SHOP_ADMIN")));

        UserDetails actual = userService.loadUserByUsername(user.getUsername());
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void shouldThrowLoadingUserIfNotFound() {
        assertThatThrownBy(() -> {
            userService.loadUserByUsername("AAAA");
        }).isInstanceOf(UsernameNotFoundException.class).hasMessage("User not found");
    }
}
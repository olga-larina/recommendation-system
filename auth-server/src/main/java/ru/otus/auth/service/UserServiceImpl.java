package ru.otus.auth.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.otus.auth.converter.DtoConverter;
import ru.otus.auth.dao.UserDao;
import ru.otus.auth.domain.User;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final DtoConverter<User, UserDetails> userConverter;

    public UserServiceImpl(UserDao userDao, DtoConverter<User, UserDetails> userConverter) {
        this.userDao = userDao;
        this.userConverter = userConverter;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userDao.findByUsername(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        // Конвертация в объекты Spring (а не имплементация интерфейсов UserDetails и GrantedAuthority) - решение проблемы:
        // The class with ru.otus.auth.domain.User and name of ru.otus.auth.domain.User is not in the allowlist
        // org.springframework.security.jackson2.SecurityJackson2Modules$AllowlistTypeIdResolver.typeFromId
        return userConverter.toDto(user.get());
    }
}

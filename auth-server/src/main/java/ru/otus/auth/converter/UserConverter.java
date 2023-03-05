package ru.otus.auth.converter;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ru.otus.auth.domain.Role;

import java.util.stream.Collectors;

@Component
public class UserConverter implements DtoConverter<ru.otus.auth.domain.User, UserDetails> {

    private final DtoConverter<Role, GrantedAuthority> roleConverter;

    public UserConverter(DtoConverter<Role, GrantedAuthority> roleConverter) {
        this.roleConverter = roleConverter;
    }

    @Override
    public UserDetails toDto(ru.otus.auth.domain.User user) {
        return new User(user.getUsername(), user.getPassword(), true, true, true, true,
            user.getAuthorities().stream().map(roleConverter::toDto).collect(Collectors.toList()));
    }
}

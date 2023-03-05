package ru.otus.auth.converter;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import ru.otus.auth.domain.Role;

@Component
public class RoleConverter implements DtoConverter<Role, GrantedAuthority> {

    @Override
    public GrantedAuthority toDto(Role role) {
        return new SimpleGrantedAuthority(role.getAuthority());
    }
}

package ru.otus.auth.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.auth.domain.User;

import java.util.Optional;

public interface UserDao extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
}

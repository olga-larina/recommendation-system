package ru.otus.auth.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.auth.domain.UserClaim;

import java.util.Optional;

public interface UserClaimDao extends JpaRepository<UserClaim, Long> {

    Optional<UserClaim> findByUsername(String username);
}

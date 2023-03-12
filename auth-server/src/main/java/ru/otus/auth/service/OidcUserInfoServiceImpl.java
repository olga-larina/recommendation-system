package ru.otus.auth.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.stereotype.Service;
import ru.otus.auth.dao.UserClaimDao;
import ru.otus.auth.domain.UserClaim;

import java.util.Optional;

@Service
public class OidcUserInfoServiceImpl implements OidcUserInfoService {

    private final UserClaimDao userClaimDao;

    public OidcUserInfoServiceImpl(UserClaimDao userClaimDao) {
        this.userClaimDao = userClaimDao;
    }

    @Override
    public OidcUserInfo loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserClaim> user = userClaimDao.findByUsername(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        UserClaim userClaim = user.get();
        return OidcUserInfo.builder()
            .name(userClaim.getUsername())
            .claim("client_id", userClaim.getClientId())
            .build();
    }
}

package ru.otus.auth.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.auth.domain.User;
import ru.otus.auth.domain.UserClaim;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserClaimDaoTest {

    @Autowired
    private UserClaimDao userClaimDao;

    @Autowired
    private TestEntityManager em;

    @Test
    void shouldFindClaimByUsername() {
        User user = new User().setUsername("testUser").setPassword("pwd");
        em.persist(user);

        UserClaim claimToInsert = new UserClaim().setUsername("testUser").setClientId("testClient");
        UserClaim claimInserted = userClaimDao.save(claimToInsert);
        UserClaim claimFound = em.find(UserClaim.class, claimInserted.getId());
        assertThat(claimFound).usingRecursiveComparison().ignoringFields("id").isEqualTo(claimToInsert);
        assertThat(claimFound.getId()).isNotNull();

        Optional<UserClaim> claimFoundByDao = userClaimDao.findByUsername(claimFound.getUsername());
        assertThat(claimFoundByDao).isNotEmpty();
        assertThat(claimFoundByDao.get()).usingRecursiveComparison().isEqualTo(claimFound);
    }

}
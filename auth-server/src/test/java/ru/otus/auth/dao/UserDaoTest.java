package ru.otus.auth.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.auth.domain.Role;
import ru.otus.auth.domain.User;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserDaoTest {

    @Autowired
    private UserDao userDao;

    @Autowired
    private TestEntityManager em;

    @Test
    void shouldFindUserByUsername() {
        User userToInsert = new User().setUsername("testUser").setPassword("pwd").setAuthorities(Set.of(Role.PRODUCT_USER, Role.RECOMMENDATION_USER, Role.SHOP_ADMIN));
        User userInserted = userDao.save(userToInsert);
        User userFound = em.find(User.class, userInserted.getId());
        assertThat(userFound).usingRecursiveComparison().ignoringFields("id").isEqualTo(userToInsert);
        assertThat(userFound.getId()).isNotNull();

        Optional<User> userFoundByDao = userDao.findByUsername(userFound.getUsername());
        assertThat(userFoundByDao).isNotEmpty();
        assertThat(userFoundByDao.get()).usingRecursiveComparison().isEqualTo(userFound);
    }

}
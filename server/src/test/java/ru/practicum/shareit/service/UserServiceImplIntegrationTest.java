package ru.practicum.shareit.service;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.model.user.User;
import ru.practicum.shareit.service.user.UserService;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class UserServiceImplIntegrationTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private UserService userService;

    @Test
    void testGetUserById() {
        User user = new User();
        user.setName("Test User");
        user.setEmail("test@user.com");
        em.persist(user);
        em.flush();

        User foundUser = userService.getUserById(user.getId());

        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getId()).isEqualTo(user.getId());
        assertThat(foundUser.getName()).isEqualTo("Test User");
    }
}
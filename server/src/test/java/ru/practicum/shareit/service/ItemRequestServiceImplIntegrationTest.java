package ru.practicum.shareit.service;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.model.user.User;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class ItemRequestServiceImplIntegrationTest {

    @Autowired
    private EntityManager em;
    @Autowired
    private ItemRequestService itemRequestService;

    @Test
    void testGetAllRequests() {
        User user1 = new User();
        user1.setName("User 1");
        user1.setEmail("user1@mail.com");
        em.persist(user1);

        User user2 = new User();
        user2.setName("User 2");
        user2.setEmail("user2@mail.com");
        em.persist(user2);

        ItemRequest request1 = new ItemRequest();
        request1.setDescription("request by user1");
        request1.setRequestor(user1);
        request1.setCreated(LocalDateTime.now());
        em.persist(request1);

        ItemRequest request2 = new ItemRequest();
        request2.setDescription("request by user2");
        request2.setRequestor(user2);
        request2.setCreated(LocalDateTime.now().plusSeconds(1));
        em.persist(request2);
        em.flush();

        List<ItemRequest> requestsForUser1 = itemRequestService.getAllRequests(user1.getId(), 0, 10);

        assertThat(requestsForUser1).hasSize(1);
        assertThat(requestsForUser1.get(0).getDescription()).isEqualTo("request by user2");
    }
}
package ru.practicum.shareit.service;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.dto.booking.BookingRequestDto;
import ru.practicum.shareit.dto.booking.enums.BookingStatus;
import ru.practicum.shareit.model.booking.Booking;
import ru.practicum.shareit.model.item.Item;
import ru.practicum.shareit.model.user.User;
import ru.practicum.shareit.service.booking.BookingService;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class BookingServiceImplIntegrationTest {

    @Autowired
    private EntityManager em;
    @Autowired
    private BookingService bookingService;

    @Test
    void testCreateBooking() {
        User owner = new User();
        owner.setName("Owner");
        owner.setEmail("owner@mail.com");
        em.persist(owner);

        User booker = new User();
        booker.setName("Booker");
        booker.setEmail("booker@mail.com");
        em.persist(booker);

        Item item = new Item();
        item.setName("Item 1");
        item.setDescription("Description 1");
        item.setAvailable(true);
        item.setOwner(owner);
        em.persist(item);
        em.flush();

        BookingRequestDto bookingDto = new BookingRequestDto();
        bookingDto.setItemId(item.getId());
        bookingDto.setStart(LocalDateTime.now().plusDays(1));
        bookingDto.setEnd(LocalDateTime.now().plusDays(2));

        Booking result = bookingService.create(booker.getId(), bookingDto);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isNotNull();
        assertThat(result.getStatus()).isEqualTo(BookingStatus.WAITING);
        assertThat(result.getBooker().getId()).isEqualTo(booker.getId());
        assertThat(result.getItem().getId()).isEqualTo(item.getId());
    }
}
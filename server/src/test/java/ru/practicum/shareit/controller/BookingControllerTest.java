package ru.practicum.shareit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.controller.booking.BookingController;
import ru.practicum.shareit.dto.booking.BookingRequestDto;
import ru.practicum.shareit.dto.booking.enums.BookingStatus;
import ru.practicum.shareit.model.booking.Booking;
import ru.practicum.shareit.model.item.Item;
import ru.practicum.shareit.model.user.User;
import ru.practicum.shareit.service.booking.BookingService;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
public class BookingControllerTest {
    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @MockBean
    private BookingService bookingService;

    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void createBooking_shouldReturnBooking() throws Exception {
        long userId = 1L;
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(2);

        BookingRequestDto bookingToCreate = new BookingRequestDto();
        bookingToCreate.setItemId(1L);
        bookingToCreate.setStart(start);
        bookingToCreate.setEnd(end);

        Booking createdBooking = new Booking();
        createdBooking.setId(1L);
        createdBooking.setStatus(BookingStatus.WAITING);
        createdBooking.setBooker(new User());
        createdBooking.setItem(new Item());
        createdBooking.setStart(start);
        createdBooking.setEnd(end);

        when(bookingService.create(anyLong(), any(BookingRequestDto.class))).thenReturn(createdBooking);

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingToCreate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.status").value("WAITING"));
    }
}

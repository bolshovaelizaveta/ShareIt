package ru.practicum.shareit.dto.booking;

import lombok.Data;
import ru.practicum.shareit.model.booking.BookingStatus;

import java.time.LocalDateTime;

@Data
public class BookingResponseDto {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private BookingStatus status;
    private BookerInBookingDto booker;
    private ItemInBookingDto item;
}

package ru.practicum.shareit.dto.booking;

import lombok.Data;
import ru.practicum.shareit.model.booking.BookingStatus;

import java.time.LocalDateTime;

@Data
public class BookingResponseDto {
    private long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private BookingStatus status;
    private BookerDto booker;
    private ItemDto item;

    @Data
    public static class BookerDto {
        private long id;
    }

    @Data
    public static class ItemDto {
        private long id;
        private String name;
    }

    @Data
    public static class ItemBookingDto {
        private long id;
        private long bookerId;
    }
}

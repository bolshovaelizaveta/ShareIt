package ru.practicum.shareit.service.booking;

import ru.practicum.shareit.dto.booking.BookingRequestDto;
import ru.practicum.shareit.model.booking.Booking;

import java.util.List;

public interface BookingService {

    Booking create(long userId, BookingRequestDto bookingDto);

    Booking approve(long userId, long bookingId, boolean approved);

    Booking getBookingById(long userId, long bookingId);

    List<Booking> getBookingsByUser(long userId, String state);

    List<Booking> getBookingsByOwner(long userId, String state);
}

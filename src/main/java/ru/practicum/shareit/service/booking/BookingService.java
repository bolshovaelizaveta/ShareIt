package ru.practicum.shareit.service.booking;

import ru.practicum.shareit.dto.booking.BookingRequestDto;
import ru.practicum.shareit.model.booking.Booking;

import java.util.List;

public interface BookingService {

    Booking create(Long userId, BookingRequestDto bookingDto);

    Booking approve(Long userId, Long bookingId, boolean approved);

    Booking getBookingById(Long userId, Long bookingId);

    List<Booking> getBookingsByUser(Long userId, String state);

    List<Booking> getBookingsByOwner(Long userId, String state);
}

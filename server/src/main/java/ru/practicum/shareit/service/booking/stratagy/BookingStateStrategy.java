package ru.practicum.shareit.service.booking.stratagy;

import ru.practicum.shareit.model.booking.Booking;
import java.util.List;

@FunctionalInterface
public interface BookingStateStrategy {
    List<Booking> findBookings(Long userId);
}
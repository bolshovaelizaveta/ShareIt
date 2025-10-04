package ru.practicum.shareit.service.booking.stratagy;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.model.booking.Booking;
import ru.practicum.shareit.storage.booking.BookingRepository;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GetFutureByOwner implements BookingStateStrategy {
    private final BookingRepository bookingRepository;
    private final Sort sort = Sort.by(Sort.Direction.DESC, "start");

    @Override
    public List<Booking> findBookings(Long userId) {
        return bookingRepository.findByItemOwnerIdAndStartIsAfter(userId, LocalDateTime.now(), sort);
    }
}
package ru.practicum.shareit.storage.booking;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.model.booking.Booking;
import ru.practicum.shareit.model.booking.BookingStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByBookerId(long bookerId, Sort sort);

    List<Booking> findByItemOwnerId(long ownerId, Sort sort);

    List<Booking> findByBookerIdAndItemIdAndStatusAndEndIsBefore(long bookerId, long itemId, BookingStatus status, LocalDateTime now);

    Optional<Booking> findFirstByItemIdAndStartBeforeAndStatus(long itemId, LocalDateTime now, BookingStatus status, Sort sort);

    Optional<Booking> findFirstByItemIdAndStartAfterAndStatus(long itemId, LocalDateTime now, BookingStatus status, Sort sort);

    List<Booking> findByItemIdInAndStatus(List<Long> itemIds, BookingStatus status, Sort sort);
}
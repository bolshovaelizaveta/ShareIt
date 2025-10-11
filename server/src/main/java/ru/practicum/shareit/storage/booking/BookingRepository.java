package ru.practicum.shareit.storage.booking;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.model.booking.Booking;
import ru.practicum.shareit.dto.booking.enums.BookingStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByBookerId(Long bookerId, Sort sort);

    List<Booking> findByItemOwnerId(Long ownerId, Sort sort);

    List<Booking> findByBookerIdAndItemIdAndStatusAndEndIsBefore(Long bookerId, Long itemId, BookingStatus status, LocalDateTime now);

    Optional<Booking> findFirstByItemIdAndStartBeforeAndStatus(Long itemId, LocalDateTime now, BookingStatus status, Sort sort);

    Optional<Booking> findFirstByItemIdAndStartAfterAndStatus(Long itemId, LocalDateTime now, BookingStatus status, Sort sort);

    List<Booking> findByItemIdInAndStatus(List<Long> itemIds, BookingStatus status, Sort sort);

    List<Booking> findByBookerIdAndStartIsBeforeAndEndIsAfter(Long bookerId, LocalDateTime start, LocalDateTime end, Sort sort);
    List<Booking> findByItemOwnerIdAndStartIsBeforeAndEndIsAfter(Long ownerId, LocalDateTime start, LocalDateTime end, Sort sort);

    List<Booking> findByBookerIdAndEndIsBefore(Long bookerId, LocalDateTime end, Sort sort);
    List<Booking> findByItemOwnerIdAndEndIsBefore(Long ownerId, LocalDateTime end, Sort sort);

    List<Booking> findByBookerIdAndStartIsAfter(Long bookerId, LocalDateTime start, Sort sort);
    List<Booking> findByItemOwnerIdAndStartIsAfter(Long ownerId, LocalDateTime start, Sort sort);

    List<Booking> findByBookerIdAndStatus(Long bookerId, BookingStatus status, Sort sort);
    List<Booking> findByItemOwnerIdAndStatus(Long ownerId, BookingStatus status, Sort sort);
}
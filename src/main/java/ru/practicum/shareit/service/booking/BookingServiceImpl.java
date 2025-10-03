package ru.practicum.shareit.service.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.dto.booking.BookingRequestDto;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.model.booking.Booking;
import ru.practicum.shareit.model.booking.BookingStatus;
import ru.practicum.shareit.model.item.Item;
import ru.practicum.shareit.model.user.User;
import ru.practicum.shareit.storage.booking.BookingRepository;
import ru.practicum.shareit.storage.item.ItemRepository;
import ru.practicum.shareit.storage.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    @Transactional
    public Booking create(long userId, BookingRequestDto bookingDto) {
        if (bookingDto.getStart().isAfter(bookingDto.getEnd()) || bookingDto.getStart().isEqual(bookingDto.getEnd())) {
            throw new ValidationException("Дата окончания не может быть раньше или равна дате начала");
        }

        User booker = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + userId + " не найден"));
        Item item = itemRepository.findById(bookingDto.getItemId())
                .orElseThrow(() -> new NotFoundException("Вещь с id " + bookingDto.getItemId() + " не найдена"));

        if (!item.getAvailable()) {
            throw new ValidationException("Вещь с id " + item.getId() + " недоступна для бронирования");
        }

        if (item.getOwner().getId() == userId) {
            throw new NotFoundException("Владелец не может забронировать свою же вещь");
        }

        Booking booking = new Booking();
        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(BookingStatus.WAITING);

        return bookingRepository.save(booking);
    }


    @Override
    @Transactional
    public Booking approve(long userId, long bookingId, boolean approved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование с id " + bookingId + " не найдено"));

        if (booking.getItem().getOwner().getId() != userId) {
            throw new ForbiddenException("Пользователь с id " + userId +
                    " не является владельцем вещи и не может изменять статус бронирования.");
        }

        if (booking.getStatus() != BookingStatus.WAITING) {
            throw new ValidationException("Бронирование уже имеет статус: " + booking.getStatus());
        }

        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        return bookingRepository.save(booking);
    }

    @Override
    public Booking getBookingById(long userId, long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование с id " + bookingId + " не найдено"));

        if (booking.getBooker().getId() != userId && booking.getItem().getOwner().getId() != userId) {
            throw new NotFoundException("Доступ к бронированию запрещен");
        }
        return booking;
    }

    @Override
    public List<Booking> getBookingsByUser(long userId, String state) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }
        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookings = bookingRepository.findByBookerId(userId, sort);

        return filterByState(bookings, state);
    }

    @Override
    public List<Booking> getBookingsByOwner(long userId, String state) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }
        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        List<Booking> bookings = bookingRepository.findByItemOwnerId(userId, sort);

        return filterByState(bookings, state);
    }

    private List<Booking> filterByState(List<Booking> bookings, String state) {
        LocalDateTime now = LocalDateTime.now();
        switch (state.toUpperCase()) {
            case "ALL":
                return bookings;
            case "CURRENT":
                return bookings.stream()
                        .filter(b -> now.isAfter(b.getStart()) && now.isBefore(b.getEnd()))
                        .collect(Collectors.toList());
            case "PAST":
                return bookings.stream()
                        .filter(b -> now.isAfter(b.getEnd()))
                        .collect(Collectors.toList());
            case "FUTURE":
                return bookings.stream()
                        .filter(b -> now.isBefore(b.getStart()))
                        .collect(Collectors.toList());
            case "WAITING":
                return bookings.stream()
                        .filter(b -> b.getStatus() == BookingStatus.WAITING)
                        .collect(Collectors.toList());
            case "REJECTED":
                return bookings.stream()
                        .filter(b -> b.getStatus() == BookingStatus.REJECTED)
                        .collect(Collectors.toList());
            default:
                throw new ValidationException("Unknown state: " + state);
        }
    }
}

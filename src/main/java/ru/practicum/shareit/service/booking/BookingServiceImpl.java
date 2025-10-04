package ru.practicum.shareit.service.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.dto.booking.BookingRequestDto;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.model.booking.Booking;
import ru.practicum.shareit.model.booking.BookingStatus;
import ru.practicum.shareit.model.booking.enums.State;
import ru.practicum.shareit.model.item.Item;
import ru.practicum.shareit.model.user.User;
import ru.practicum.shareit.service.booking.stratagy.BookingStateStrategy;
import ru.practicum.shareit.storage.booking.BookingRepository;
import ru.practicum.shareit.storage.item.ItemRepository;
import ru.practicum.shareit.storage.user.UserRepository;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final Map<String, BookingStateStrategy> bookerStrategies;
    private final Map<String, BookingStateStrategy> ownerStrategies;

    @Override
    @Transactional
    public Booking create(Long userId, BookingRequestDto bookingDto) {

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
    public Booking approve(Long userId, Long bookingId, boolean approved) {
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
    public Booking getBookingById(Long userId, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование с id " + bookingId + " не найдено"));

        if (booking.getBooker().getId() != userId && booking.getItem().getOwner().getId() != userId) {
            throw new NotFoundException("Доступ к бронированию запрещен");
        }
        return booking;
    }

    @Override
    public List<Booking> getBookingsByUser(Long userId, String stateString) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id ".concat(String.valueOf(userId)).concat(" не найден")));

        State state = State.from(stateString)
                .orElseThrow(() -> new ValidationException("Unknown state: ".concat(stateString)));

        String strategyName = "get" + state.name().substring(0, 1) + state.name().substring(1).toLowerCase() + "ByBooker";

        BookingStateStrategy strategy = bookerStrategies.get(strategyName);
        return strategy.findBookings(userId);
    }

    @Override
    public List<Booking> getBookingsByOwner(Long userId, String stateString) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id ".concat(String.valueOf(userId)).concat(" не найден")));

        State state = State.from(stateString)
                .orElseThrow(() -> new ValidationException("Unknown state: ".concat(stateString)));

        String strategyName = "get" + state.name().substring(0, 1) + state.name().substring(1).toLowerCase() + "ByOwner";

        BookingStateStrategy strategy = ownerStrategies.get(strategyName);
        return strategy.findBookings(userId);
    }
}

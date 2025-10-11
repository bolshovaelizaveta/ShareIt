package ru.practicum.shareit.controller.booking;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.dto.booking.BookingRequestDto;
import ru.practicum.shareit.client.BookingClient;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {

    private final BookingClient bookingClient;
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader(USER_ID_HEADER) Long userId,
                                         @Valid @RequestBody BookingRequestDto bookingDto) {
        log.info("Получен запрос POST /bookings от пользователя {} с телом: {}", userId, bookingDto);
        return bookingClient.createBooking(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approve(@RequestHeader(USER_ID_HEADER) Long userId,
                                          @PathVariable Long bookingId,
                                          @RequestParam boolean approved) {
        log.info("Получен запрос PATCH /bookings/{} от пользователя {} с approved={}", bookingId, userId, approved);
        return bookingClient.approveBooking(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingById(@RequestHeader(USER_ID_HEADER) Long userId,
                                                 @PathVariable Long bookingId) {
        log.info("Получен запрос GET /bookings/{} от пользователя {}", bookingId, userId);
        return bookingClient.getBookingById(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> getBookingsByUser(@RequestHeader(USER_ID_HEADER) Long userId,
                                                    @RequestParam(defaultValue = "ALL") String state,
                                                    @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                    @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("Получен запрос GET /bookings?state={} от пользователя {}", state, userId);
        return bookingClient.getBookingsByUser(userId, state, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getBookingsByOwner(@RequestHeader(USER_ID_HEADER) Long userId,
                                                     @RequestParam(defaultValue = "ALL") String state,
                                                     @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                     @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("Получен запрос GET /bookings/owner?state={} от пользователя {}", state, userId);
        return bookingClient.getBookingsByOwner(userId, state, from, size);
    }
}
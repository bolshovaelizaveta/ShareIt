package ru.practicum.shareit.controller.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.dto.booking.BookingRequestDto;
import ru.practicum.shareit.dto.booking.BookingResponseDto;
import ru.practicum.shareit.mapper.booking.BookingMapper;
import ru.practicum.shareit.model.booking.Booking;
import ru.practicum.shareit.service.booking.BookingService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {

    private final BookingService bookingService;
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @PostMapping
    public BookingResponseDto create(@RequestHeader(USER_ID_HEADER) Long userId,
                                     @RequestBody BookingRequestDto bookingDto) {
        log.info("Server: POST /bookings, userId={}, bookingDto={}", userId, bookingDto);
        Booking createdBooking = bookingService.create(userId, bookingDto);
        return BookingMapper.toBookingResponseDto(createdBooking);
    }

    @PatchMapping("/{bookingId}")
    public BookingResponseDto approve(@RequestHeader(USER_ID_HEADER) Long userId,
                                      @PathVariable Long bookingId,
                                      @RequestParam boolean approved) {
        log.info("Server: PATCH /bookings/{}, userId={}, approved={}", bookingId, userId, approved);
        Booking approvedBooking = bookingService.approve(userId, bookingId, approved);
        return BookingMapper.toBookingResponseDto(approvedBooking);
    }

    @GetMapping("/{bookingId}")
    public BookingResponseDto getBookingById(@RequestHeader(USER_ID_HEADER) Long userId,
                                             @PathVariable Long bookingId) {
        log.info("Server: GET /bookings/{}, userId={}", bookingId, userId);
        Booking booking = bookingService.getBookingById(userId, bookingId);
        return BookingMapper.toBookingResponseDto(booking);
    }

    @GetMapping
    public List<BookingResponseDto> getBookingsByUser(@RequestHeader(USER_ID_HEADER) Long userId,
                                                      @RequestParam(defaultValue = "ALL") String state,
                                                      @RequestParam(defaultValue = "0") Integer from,
                                                      @RequestParam(defaultValue = "10") Integer size) {
        log.info("Server: GET /bookings?state={}, userId={}, from={}, size={}", state, userId, from, size);
        return bookingService.getBookingsByUser(userId, state, from, size).stream()
                .map(BookingMapper::toBookingResponseDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/owner")
    public List<BookingResponseDto> getBookingsByOwner(@RequestHeader(USER_ID_HEADER) Long userId,
                                                       @RequestParam(defaultValue = "ALL") String state,
                                                       @RequestParam(defaultValue = "0") Integer from,
                                                       @RequestParam(defaultValue = "10") Integer size) {
        log.info("Server: GET /bookings/owner?state={}, userId={}, from={}, size={}", state, userId, from, size);
        return bookingService.getBookingsByOwner(userId, state, from, size).stream()
                .map(BookingMapper::toBookingResponseDto)
                .collect(Collectors.toList());
    }
}
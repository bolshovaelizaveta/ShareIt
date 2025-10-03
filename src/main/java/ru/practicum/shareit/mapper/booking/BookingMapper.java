package ru.practicum.shareit.mapper.booking;

import ru.practicum.shareit.dto.booking.BookingResponseDto;
import ru.practicum.shareit.model.booking.Booking;

public class BookingMapper {
    public static BookingResponseDto toBookingResponseDto(Booking booking) {
        BookingResponseDto dto = new BookingResponseDto();
        dto.setId(booking.getId());
        dto.setStart(booking.getStart());
        dto.setEnd(booking.getEnd());
        dto.setStatus(booking.getStatus());

        BookingResponseDto.BookerDto bookerDto = new BookingResponseDto.BookerDto();
        bookerDto.setId(booking.getBooker().getId());
        dto.setBooker(bookerDto);

        BookingResponseDto.ItemDto itemDto = new BookingResponseDto.ItemDto();
        itemDto.setId(booking.getItem().getId());
        itemDto.setName(booking.getItem().getName());
        dto.setItem(itemDto);

        return dto;
    }

    public static BookingResponseDto.ItemBookingDto toItemBookingDto(Booking booking) {
        if (booking == null) return null;
        BookingResponseDto.ItemBookingDto dto = new BookingResponseDto.ItemBookingDto();
        dto.setId(booking.getId());
        dto.setBookerId(booking.getBooker().getId());
        return dto;
    }
}
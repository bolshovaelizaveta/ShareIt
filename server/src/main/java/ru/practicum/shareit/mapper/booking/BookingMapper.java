package ru.practicum.shareit.mapper.booking;

import ru.practicum.shareit.dto.booking.BookingForItemDto;
import ru.practicum.shareit.dto.booking.BookingResponseDto;
import ru.practicum.shareit.dto.booking.BookerInBookingDto;
import ru.practicum.shareit.dto.booking.ItemInBookingDto;
import ru.practicum.shareit.model.booking.Booking;

public class BookingMapper {
    public static BookingResponseDto toBookingResponseDto(Booking booking) {
        BookingResponseDto dto = new BookingResponseDto();
        dto.setId(booking.getId());
        dto.setStart(booking.getStart());
        dto.setEnd(booking.getEnd());
        dto.setStatus(booking.getStatus());

        BookerInBookingDto bookerDto = new BookerInBookingDto();
        bookerDto.setId(booking.getBooker().getId());
        dto.setBooker(bookerDto);

        ItemInBookingDto itemDto = new ItemInBookingDto();
        itemDto.setId(booking.getItem().getId());
        itemDto.setName(booking.getItem().getName());
        dto.setItem(itemDto);

        return dto;
    }

    public static BookingForItemDto toItemBookingDto(Booking booking) {
        if (booking == null) return null;
        BookingForItemDto dto = new BookingForItemDto();
        dto.setId(booking.getId());
        dto.setBookerId(booking.getBooker().getId());
        return dto;
    }
}
package ru.practicum.shareit.dto.item;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.practicum.shareit.dto.booking.BookingResponseDto;
import ru.practicum.shareit.dto.comment.CommentDto;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class ItemWithBookingsDto extends ItemDto {
    private BookingResponseDto.ItemBookingDto lastBooking;
    private BookingResponseDto.ItemBookingDto nextBooking;
    private List<CommentDto> comments;
}
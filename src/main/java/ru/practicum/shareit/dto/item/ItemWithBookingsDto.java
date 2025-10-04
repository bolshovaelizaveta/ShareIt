package ru.practicum.shareit.dto.item;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.practicum.shareit.dto.booking.BookingForItemDto;
import ru.practicum.shareit.dto.comment.CommentDto;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class ItemWithBookingsDto extends ItemDto {
    private BookingForItemDto lastBooking;
    private BookingForItemDto nextBooking;
    private List<CommentDto> comments;
}
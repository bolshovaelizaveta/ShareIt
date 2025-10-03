package ru.practicum.shareit.service.item;

import ru.practicum.shareit.dto.comment.CommentDto;
import ru.practicum.shareit.dto.item.ItemDto;
import ru.practicum.shareit.dto.item.ItemWithBookingsDto;
import ru.practicum.shareit.model.item.Item;
import java.util.List;

public interface ItemService {

    Item create(long userId, Item item);

    Item update(long userId, long itemId, ItemDto itemDto);

    ItemWithBookingsDto getItemById(long userId, long itemId);

    List<ItemWithBookingsDto> getItemsByOwner(long userId);

    List<Item> search(String text);

    CommentDto addComment(long userId, long itemId, CommentDto commentDto);
}
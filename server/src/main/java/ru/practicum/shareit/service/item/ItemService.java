package ru.practicum.shareit.service.item;

import ru.practicum.shareit.dto.comment.CommentDto;
import ru.practicum.shareit.dto.item.ItemDto;
import ru.practicum.shareit.dto.item.ItemWithBookingsDto;
import ru.practicum.shareit.model.item.Item;
import java.util.List;

public interface ItemService {

    Item create(Long userId, Item item, Long requestId);

    Item update(Long userId, Long itemId, ItemDto itemDto);

    ItemWithBookingsDto getItemById(Long userId, Long itemId);

    List<ItemWithBookingsDto> getItemsByOwner(Long userId);

    List<Item> search(String text);

    CommentDto addComment(Long userId, Long itemId, CommentDto commentDto);
}
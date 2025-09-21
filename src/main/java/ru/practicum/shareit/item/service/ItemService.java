package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

    Item create(long userId, Item item);

    Item update(long userId, long itemId, ItemDto itemDto);

    Item getItemById(long itemId);

    List<Item> getItemsByOwner(long userId);

    List<Item> search(String text);
}
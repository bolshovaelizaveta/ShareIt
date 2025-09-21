package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.Item;

import java.util.List;

public interface ItemStorage {

    Item create(Item item);

    Item update(Item item);

    Item getItemById(long id);

    List<Item> getItemsByOwner(long ownerId);

    List<Item> search(String text);
}
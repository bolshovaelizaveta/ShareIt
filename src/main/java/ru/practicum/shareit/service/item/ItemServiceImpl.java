package ru.practicum.shareit.service.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.model.item.Item;
import ru.practicum.shareit.dto.item.ItemDto;
import ru.practicum.shareit.service.user.UserService;
import ru.practicum.shareit.storage.item.ItemStorage;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemStorage itemStorage;
    private final UserService userService;

    @Override
    public Item create(long userId, Item item) {
        item.setOwner(userService.getUserById(userId));
        return itemStorage.create(item);
    }

    @Override
    public Item update(long userId, long itemId, ItemDto itemDto) {
        userService.getUserById(userId);

        Item existingItem = itemStorage.getItemById(itemId);
        if (existingItem == null) {
            throw new NotFoundException("Вещь с id " + itemId + " не найдена");
        }

        if (existingItem.getOwner().getId() != userId) {
            throw new NotFoundException("Пользователь " + userId + " не является владельцем вещи " + itemId);
        }

        if (itemDto.getName() != null) {
            existingItem.setName(itemDto.getName());
        }

        if (itemDto.getDescription() != null) {
            existingItem.setDescription(itemDto.getDescription());
        }

        if (itemDto.getAvailable() != null) {
            existingItem.setAvailable(itemDto.getAvailable());
        }
        return itemStorage.update(existingItem);
    }

    @Override
    public Item getItemById(long itemId) {
        Item item = itemStorage.getItemById(itemId);
        if (item == null) {
            throw new NotFoundException("Вещь с id " + itemId + " не найдена");
        }
        return item;
    }

    @Override
    public List<Item> getItemsByOwner(long userId) {
        userService.getUserById(userId);
        return itemStorage.getItemsByOwner(userId);
    }

    @Override
    public List<Item> search(String text) {
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        return itemStorage.search(text);
    }
}


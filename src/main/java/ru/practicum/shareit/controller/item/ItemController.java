package ru.practicum.shareit.controller.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.model.item.Item;
import ru.practicum.shareit.dto.item.ItemDto;
import ru.practicum.shareit.mapper.item.ItemMapper;
import ru.practicum.shareit.service.item.ItemService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController implements ItemControllerApi {

    private final ItemService itemService;
    public static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @Override
    public ItemDto create(@RequestHeader(USER_ID_HEADER) long userId,
                          @Valid @RequestBody ItemDto itemDto) {
        log.info("Получен запрос POST /items от пользователя {} с телом: {}", userId, itemDto);
        Item item = ItemMapper.toItem(itemDto);
        Item createdItem = itemService.create(userId, item);
        return ItemMapper.toItemDto(createdItem);
    }

    @Override
    public ItemDto update(@RequestHeader(USER_ID_HEADER) long userId,
                          @PathVariable long itemId,
                          @RequestBody ItemDto itemDto) {
        log.info("Получен запрос PATCH /items/{} от пользователя {} с телом: {}", itemId, userId, itemDto);
        Item updatedItem = itemService.update(userId, itemId, itemDto);
        return ItemMapper.toItemDto(updatedItem);
    }

    @Override
    public ItemDto getItemById(@PathVariable long itemId) {
        log.info("Получен запрос GET /items/{}", itemId);
        return ItemMapper.toItemDto(itemService.getItemById(itemId));
    }

    @Override
    public List<ItemDto> getItemsByOwner(@RequestHeader(USER_ID_HEADER) long userId) {
        log.info("Получен запрос GET /items от пользователя {}", userId);
        return itemService.getItemsByOwner(userId).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> search(@RequestParam String text) {
        log.info("Получен запрос GET /items/search?text={}", text);
        return itemService.search(text).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }
}
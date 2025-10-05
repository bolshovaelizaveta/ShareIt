package ru.practicum.shareit.controller.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.dto.comment.CommentDto;
import ru.practicum.shareit.dto.item.ItemDto;
import ru.practicum.shareit.dto.item.ItemWithBookingsDto;
import ru.practicum.shareit.mapper.item.ItemMapper;
import ru.practicum.shareit.model.item.Item;
import ru.practicum.shareit.service.item.ItemService;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController implements ItemControllerApi {

    public static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private final ItemService itemService;

    @Override
    @PostMapping
    public ItemDto create(@RequestHeader(USER_ID_HEADER) Long userId,
                          @Valid @RequestBody ItemDto itemDto) {
        log.info("Получен запрос POST /items от пользователя {} с телом: {}", userId, itemDto);
        Item item = ItemMapper.toItem(itemDto);
        Item createdItem = itemService.create(userId, item);
        return ItemMapper.toItemDto(createdItem);
    }

    @Override
    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader(USER_ID_HEADER) Long userId,
                          @PathVariable Long itemId,
                          @RequestBody ItemDto itemDto) {
        log.info("Получен запрос PATCH /items/{} от пользователя {} с телом: {}", itemId, userId, itemDto);
        Item updatedItem = itemService.update(userId, itemId, itemDto);
        return ItemMapper.toItemDto(updatedItem);
    }

    @Override
    @GetMapping("/{itemId}")
    public ItemWithBookingsDto getItemById(@RequestHeader(USER_ID_HEADER) Long userId,
                                           @PathVariable Long itemId) {
        log.info("Получен запрос GET /items/{} от пользователя {}", itemId, userId);
        return itemService.getItemById(userId, itemId);
    }

    @Override
    @GetMapping
    public List<ItemWithBookingsDto> getItemsByOwner(@RequestHeader(USER_ID_HEADER) Long userId) {
        log.info("Получен запрос GET /items от пользователя {}", userId);
        return itemService.getItemsByOwner(userId);
    }

    @Override
    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam String text) {
        log.info("Получен запрос GET /items/search?text={}", text);
        return itemService.search(text).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestHeader(USER_ID_HEADER) Long userId,
                                 @PathVariable Long itemId,
                                 @Valid @RequestBody CommentDto commentDto) {
        log.info("Получен запрос POST /items/{}/comment от пользователя {} с телом {}", itemId, userId, commentDto);
        return itemService.addComment(userId, itemId, commentDto);
    }
}
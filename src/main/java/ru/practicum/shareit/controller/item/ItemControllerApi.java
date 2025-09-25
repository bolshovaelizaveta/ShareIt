package ru.practicum.shareit.controller.item;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.dto.item.ItemDto;

import java.util.List;

public interface ItemControllerApi {

    @PostMapping
    ItemDto create(@RequestHeader(ItemController.USER_ID_HEADER) long userId,
                   @Valid @RequestBody ItemDto itemDto);

    @PatchMapping("/{itemId}")
    ItemDto update(@RequestHeader(ItemController.USER_ID_HEADER) long userId,
                   @PathVariable long itemId,
                   @RequestBody ItemDto itemDto);

    @GetMapping("/{itemId}")
    ItemDto getItemById(@PathVariable long itemId);

    @GetMapping
    List<ItemDto> getItemsByOwner(@RequestHeader(ItemController.USER_ID_HEADER) long userId);

    @GetMapping("/search")
    List<ItemDto> search(@RequestParam String text);
}
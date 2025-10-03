package ru.practicum.shareit.controller.item;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.dto.comment.CommentDto;
import ru.practicum.shareit.dto.item.ItemDto;
import ru.practicum.shareit.dto.item.ItemWithBookingsDto;

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
    ItemWithBookingsDto getItemById(@RequestHeader(ItemController.USER_ID_HEADER) long userId,
                                    @PathVariable long itemId);

    @GetMapping
    List<ItemWithBookingsDto> getItemsByOwner(@RequestHeader(ItemController.USER_ID_HEADER) long userId);

    @GetMapping("/search")
    List<ItemDto> search(@RequestParam String text);

    @PostMapping("/{itemId}/comment")
    CommentDto addComment(@RequestHeader(ItemController.USER_ID_HEADER) long userId,
                          @PathVariable long itemId,
                          @Valid @RequestBody CommentDto commentDto);
}
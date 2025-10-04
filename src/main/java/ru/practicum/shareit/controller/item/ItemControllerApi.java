package ru.practicum.shareit.controller.item;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.dto.comment.CommentDto;
import ru.practicum.shareit.dto.item.ItemDto;
import ru.practicum.shareit.dto.item.ItemWithBookingsDto;

import java.util.List;

public interface ItemControllerApi {

    @PostMapping
    ItemDto create(@RequestHeader(ItemController.USER_ID_HEADER) Long userId,
                   @Valid @RequestBody ItemDto itemDto);

    @PatchMapping("/{itemId}")
    ItemDto update(@RequestHeader(ItemController.USER_ID_HEADER) Long userId,
                   @PathVariable Long itemId,
                   @RequestBody ItemDto itemDto);

    @GetMapping("/{itemId}")
    ItemWithBookingsDto getItemById(@RequestHeader(ItemController.USER_ID_HEADER) Long userId,
                                    @PathVariable Long itemId);

    @GetMapping
    List<ItemWithBookingsDto> getItemsByOwner(@RequestHeader(ItemController.USER_ID_HEADER) Long userId);

    @GetMapping("/search")
    List<ItemDto> search(@RequestParam String text);

    @PostMapping("/{itemId}/comment")
    CommentDto addComment(@RequestHeader(ItemController.USER_ID_HEADER) Long userId,
                          @PathVariable Long itemId,
                          @Valid @RequestBody CommentDto commentDto);
}
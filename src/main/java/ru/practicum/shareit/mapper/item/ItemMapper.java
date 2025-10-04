package ru.practicum.shareit.mapper.item;

import ru.practicum.shareit.dto.item.ItemWithBookingsDto;
import ru.practicum.shareit.mapper.comment.CommentMapper;
import ru.practicum.shareit.model.comment.Comment;
import ru.practicum.shareit.model.item.Item;
import ru.practicum.shareit.dto.item.ItemDto;

import java.util.List;
import java.util.stream.Collectors;

public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());
        if (item.getRequest() != null) {
            itemDto.setRequestId(item.getRequest().getId());
        }
        return itemDto;
    }

    public static Item toItem(ItemDto itemDto) {
        Item item = new Item();
        item.setId(itemDto.getId());
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        return item;
    }

    public static ItemWithBookingsDto toItemWithBookingsDto(Item item, List<Comment> comments) {
        ItemWithBookingsDto dto = new ItemWithBookingsDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setAvailable(item.getAvailable());
        if (comments != null) {
            dto.setComments(comments.stream()
                    .map(CommentMapper::toCommentDto)
                    .collect(Collectors.toList()));
        }
        return dto;
    }
}
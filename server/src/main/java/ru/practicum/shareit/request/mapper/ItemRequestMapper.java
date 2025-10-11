package ru.practicum.shareit.request.mapper;

import ru.practicum.shareit.dto.request.ItemRequestResponseDto;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.mapper.item.ItemMapper;

import java.util.stream.Collectors;
import java.util.Collections;

public class ItemRequestMapper {

    public static ItemRequestResponseDto toResponseDto(ItemRequest itemRequest) {
        ItemRequestResponseDto dto = new ItemRequestResponseDto();
        dto.setId(itemRequest.getId());
        dto.setDescription(itemRequest.getDescription());
        dto.setCreated(itemRequest.getCreated());

        if (itemRequest.getItems() != null && !itemRequest.getItems().isEmpty()) {
            dto.setItems(itemRequest.getItems().stream()
                    .map(ItemMapper::toItemDto)
                    .collect(Collectors.toList()));
        } else {
            dto.setItems(Collections.emptyList());
        }

        return dto;
    }
}
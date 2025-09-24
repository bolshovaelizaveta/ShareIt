package ru.practicum.shareit.model.item;

import lombok.Data;
import ru.practicum.shareit.model.user.User;
import ru.practicum.shareit.request.ItemRequest;

@Data
public class Item {
    private long id;
    private String name;
    private String description;
    private boolean available;
    private User owner;
    private ItemRequest request;
}
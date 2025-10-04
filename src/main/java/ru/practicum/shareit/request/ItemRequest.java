package ru.practicum.shareit.request;

import lombok.Data;
import ru.practicum.shareit.model.user.User;
import java.time.LocalDateTime;

@Data
public class ItemRequest {
    private Long id;
    private String description;
    private User requestor;
    private LocalDateTime created;
}
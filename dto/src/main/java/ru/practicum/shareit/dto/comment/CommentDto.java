package ru.practicum.shareit.dto.comment;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDto {
    private Long id;

    @NotBlank
    private String text;

    private String authorName;
    private LocalDateTime created;
}
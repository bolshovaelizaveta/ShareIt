package ru.practicum.shareit.mapper.comment;

import ru.practicum.shareit.dto.comment.CommentDto;
import ru.practicum.shareit.model.comment.Comment;

public class CommentMapper {

    public static CommentDto toCommentDto(Comment comment) {
        CommentDto dto = new CommentDto();
        dto.setId(comment.getId());
        dto.setText(comment.getText());
        dto.setAuthorName(comment.getAuthor().getName());
        dto.setCreated(comment.getCreated());
        return dto;
    }
}
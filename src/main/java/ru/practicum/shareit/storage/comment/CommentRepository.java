package ru.practicum.shareit.storage.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.model.comment.Comment;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByItemId(long itemId);

    List<Comment> findByItemIdIn(List<Long> itemIds);
}
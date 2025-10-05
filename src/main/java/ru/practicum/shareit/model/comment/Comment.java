package ru.practicum.shareit.model.comment;

import jakarta.persistence.*;
import lombok.Data;
import ru.practicum.shareit.model.item.Item;
import ru.practicum.shareit.model.user.User;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "text", nullable = false, length = 1000)
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime created;
}

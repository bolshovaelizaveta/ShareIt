package ru.practicum.shareit.model.item;

import jakarta.persistence.*;
import lombok.Data;
import ru.practicum.shareit.model.user.User;
import ru.practicum.shareit.request.ItemRequest;

@Data
@Entity
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false, length = 1000)
    private String description;

    @Column(name = "is_available", nullable = false)
    private Boolean available;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Transient
    private ItemRequest request;
}
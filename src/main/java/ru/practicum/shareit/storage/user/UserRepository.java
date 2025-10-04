package ru.practicum.shareit.storage.user;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.model.user.User;

public interface UserRepository extends JpaRepository<User, Long> {
}

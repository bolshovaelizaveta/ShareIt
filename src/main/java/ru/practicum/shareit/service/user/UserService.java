package ru.practicum.shareit.service.user;

import ru.practicum.shareit.model.user.User;
import java.util.List;

public interface UserService {
    List<User> findAll();
    User create(User user);
    User update(Long userId, User user);
    User getUserById(Long id);
    void delete(Long id);
}
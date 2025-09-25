package ru.practicum.shareit.service.user;

import ru.practicum.shareit.model.user.User;
import java.util.List;

public interface UserService {
    List<User> findAll();
    User create(User user);
    User update(long userId, User user);
    User getUserById(long id);
    void delete(long id);
}
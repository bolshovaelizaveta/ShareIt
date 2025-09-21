package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.User;
import java.util.List;

public interface UserStorage {
    List<User> findAll();
    User create(User user);
    User update(User user);
    User getUserById(long id);
    void delete(long id);
}
package ru.practicum.shareit.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.model.user.User;
import ru.practicum.shareit.storage.user.UserStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    @Override
    public List<User> findAll() {
        return userStorage.findAll();
    }

    @Override
    public User create(User user) {
        validateEmail(user);
        return userStorage.create(user);
    }

    @Override
    public User update(long userId, User user) {
        User existingUser = getUserById(userId);
        if (user.getName() != null) {
            existingUser.setName(user.getName());
        }
        if (user.getEmail() != null && !user.getEmail().equals(existingUser.getEmail())) {
            validateEmail(user);
            existingUser.setEmail(user.getEmail());
        }
        return userStorage.update(existingUser);
    }

    @Override
    public User getUserById(long id) {
        User user = userStorage.getUserById(id);
        if (user == null) {
            throw new NotFoundException("Пользователь с id " + id + " не найден");
        }
        return user;
    }

    @Override
    public void delete(long id) {
        getUserById(id);
        userStorage.delete(id);
    }

    private void validateEmail(User user) {
        for (User u : userStorage.findAll()) {
            if (u.getEmail().equals(user.getEmail()) && u.getId() != user.getId()) {
                throw new ConflictException("Email " + user.getEmail() + " уже используется");
            }
        }
    }
}

package ru.practicum.shareit.controller.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.model.user.User;
import ru.practicum.shareit.dto.user.UserDto;
import ru.practicum.shareit.mapper.user.UserMapper;
import ru.practicum.shareit.service.user.UserService;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController implements UserControllerApi {
    private final UserService userService;

    @Override
    public List<UserDto> findAll() {
        log.info("Получен запрос GET /users");
        return userService.findAll().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto create(@Valid @RequestBody UserDto userDto) {
        log.info("Получен запрос POST /users с телом: {}", userDto);
        User user = UserMapper.toUser(userDto);
        User createdUser = userService.create(user);
        return UserMapper.toUserDto(createdUser);
    }

    @Override
    public UserDto update(@PathVariable long userId, @RequestBody UserDto userDto) {
        log.info("Получен запрос PATCH /users/{} с телом: {}", userId, userDto);
        User user = UserMapper.toUser(userDto);
        User updatedUser = userService.update(userId, user);
        return UserMapper.toUserDto(updatedUser);
    }

    @Override
    public UserDto getUserById(@PathVariable long userId) {
        log.info("Получен запрос GET /users/{}", userId);
        return UserMapper.toUserDto(userService.getUserById(userId));
    }

    @Override
    public void delete(@PathVariable long userId) {
        log.info("Получен запрос DELETE /users/{}", userId);
        userService.delete(userId);
    }
}
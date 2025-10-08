package ru.practicum.shareit.controller.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.dto.user.UserDto;
import ru.practicum.shareit.mapper.user.UserMapper;
import ru.practicum.shareit.model.user.User;
import ru.practicum.shareit.service.user.UserService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> findAll() {
        log.info("Server: получен запрос GET /users");
        return userService.findAll().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    public UserDto create(@RequestBody UserDto userDto) {
        log.info("Server: получен запрос POST /users с телом: {}", userDto);
        User user = UserMapper.toUser(userDto);
        User createdUser = userService.create(user);
        return UserMapper.toUserDto(createdUser);
    }

    @PatchMapping("/{userId}")
    public UserDto update(@PathVariable Long userId, @RequestBody UserDto userDto) {
        log.info("Server: получен запрос PATCH /users/{} с телом: {}", userId, userDto);
        User user = UserMapper.toUser(userDto);
        User updatedUser = userService.update(userId, user);
        return UserMapper.toUserDto(updatedUser);
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable Long userId) {
        log.info("Server: получен запрос GET /users/{}", userId);
        return UserMapper.toUserDto(userService.getUserById(userId));
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable Long userId) {
        log.info("Server: получен запрос DELETE /users/{}", userId);
        userService.delete(userId);
    }
}
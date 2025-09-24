package ru.practicum.shareit.controller.user;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.dto.user.UserDto;
import java.util.List;

public interface UserControllerApi {
    @GetMapping
    List<UserDto> findAll();

    @PostMapping
    UserDto create(@Valid @RequestBody UserDto userDto);

    @PatchMapping("/{userId}")
    UserDto update(@PathVariable long userId, @RequestBody UserDto userDto);

    @GetMapping("/{userId}")
    UserDto getUserById(@PathVariable long userId);

    @DeleteMapping("/{userId}")
    void delete(@PathVariable long userId);
}
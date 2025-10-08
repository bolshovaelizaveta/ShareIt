package ru.practicum.shareit.request;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.dto.request.ItemRequestDto;
import ru.practicum.shareit.dto.request.ItemRequestResponseDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
public class ItemRequestController {

    private final ItemRequestService itemRequestService;
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @PostMapping
    public ItemRequestResponseDto create(@RequestHeader(USER_ID_HEADER) Long userId,
                                         @RequestBody ItemRequestDto itemRequestDto) {
        log.info("Server: POST /requests, userId={}, requestDto={}", userId, itemRequestDto);
        ItemRequest request = new ItemRequest();
        request.setDescription(itemRequestDto.getDescription());
        ItemRequest createdRequest = itemRequestService.create(userId, request);
        return ItemRequestMapper.toResponseDto(createdRequest);
    }

    @GetMapping
    public List<ItemRequestResponseDto> getUserRequests(@RequestHeader(USER_ID_HEADER) Long userId) {
        log.info("Server: GET /requests, userId={}", userId);
        return itemRequestService.getUserRequests(userId).stream()
                .map(ItemRequestMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/all")
    public List<ItemRequestResponseDto> getAllRequests(@RequestHeader(USER_ID_HEADER) Long userId,
                                                       @RequestParam(defaultValue = "0") int from,
                                                       @RequestParam(defaultValue = "10") int size) {
        log.info("Server: GET /requests/all, userId={}, from={}, size={}", userId, from, size);
        return itemRequestService.getAllRequests(userId, from, size).stream()
                .map(ItemRequestMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{requestId}")
    public ItemRequestResponseDto getRequestById(@RequestHeader(USER_ID_HEADER) Long userId,
                                                 @PathVariable Long requestId) {
        log.info("Server: GET /requests/{}, userId={}", requestId, userId);
        ItemRequest request = itemRequestService.getRequestById(userId, requestId);
        return ItemRequestMapper.toResponseDto(request);
    }
}
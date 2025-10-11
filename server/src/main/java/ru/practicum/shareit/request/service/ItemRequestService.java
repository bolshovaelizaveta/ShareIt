package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.ItemRequest;
import java.util.List;

public interface ItemRequestService {

    ItemRequest create(Long userId, ItemRequest itemRequest);

    List<ItemRequest> getUserRequests(Long userId);

    List<ItemRequest> getAllRequests(Long userId, int from, int size);

    ItemRequest getRequestById(Long userId, Long requestId);
}
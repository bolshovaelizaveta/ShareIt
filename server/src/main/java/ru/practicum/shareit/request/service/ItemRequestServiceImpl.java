package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.model.user.User;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.storage.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ItemRequest create(Long userId, ItemRequest itemRequest) {
        User requestor = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + userId + " не найден"));

        itemRequest.setRequestor(requestor);
        itemRequest.setCreated(LocalDateTime.now());

        return itemRequestRepository.save(itemRequest);
    }

    @Override
    public List<ItemRequest> getUserRequests(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + userId + " не найден"));
        Sort sort = Sort.by(Sort.Direction.DESC, "created");
        return itemRequestRepository.findByRequestorId(userId, sort);
    }

    @Override
    public List<ItemRequest> getAllRequests(Long userId, int from, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "created");
        PageRequest pageable = PageRequest.of(from > 0 ? from / size : 0, size, sort);
        return itemRequestRepository.findByRequestorIdNot(userId, pageable);
    }

    @Override
    public ItemRequest getRequestById(Long userId, Long requestId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + userId + " не найден"));
        return itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Запрос с id " + requestId + " не найден"));
    }
}
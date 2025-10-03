package ru.practicum.shareit.service.item;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.dto.comment.CommentDto;
import ru.practicum.shareit.dto.item.ItemDto;
import ru.practicum.shareit.dto.item.ItemWithBookingsDto;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.mapper.booking.BookingMapper;
import ru.practicum.shareit.mapper.comment.CommentMapper;
import ru.practicum.shareit.mapper.item.ItemMapper;
import ru.practicum.shareit.model.booking.Booking;
import ru.practicum.shareit.model.booking.BookingStatus;
import ru.practicum.shareit.model.comment.Comment;
import ru.practicum.shareit.model.item.Item;
import ru.practicum.shareit.model.user.User;
import ru.practicum.shareit.storage.booking.BookingRepository;
import ru.practicum.shareit.storage.comment.CommentRepository;
import ru.practicum.shareit.storage.item.ItemRepository;
import ru.practicum.shareit.storage.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public Item create(long userId, Item item) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + userId + " не найден"));
        item.setOwner(owner);
        return itemRepository.save(item);
    }

    @Override
    @Transactional
    public Item update(long userId, long itemId, ItemDto itemDto) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + userId + " не найден"));
        Item existingItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с id " + itemId + " не найдена"));

        if (existingItem.getOwner().getId() != userId) {
            throw new NotFoundException("Пользователь не является владельцем вещи");
        }

        if (itemDto.getName() != null) {
            existingItem.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            existingItem.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            existingItem.setAvailable(itemDto.getAvailable());
        }

        return itemRepository.save(existingItem);
    }

    @Override
    public ItemWithBookingsDto getItemById(long userId, long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с id " + itemId + " не найдена"));
        List<Comment> comments = commentRepository.findByItemId(itemId);
        ItemWithBookingsDto dto = ItemMapper.toItemWithBookingsDto(item, comments);

        if (item.getOwner().getId() == userId) {
            addBookingsToItemDto(dto);
        }
        return dto;
    }

    @Override
    public List<ItemWithBookingsDto> getItemsByOwner(long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + userId + " не найден"));

        List<Item> items = itemRepository.findByOwnerId(userId, Sort.by(Sort.Direction.ASC, "id"));
        if (items.isEmpty()) {
            return Collections.emptyList();
        }
        List<Long> itemIds = items.stream().map(Item::getId).collect(Collectors.toList());

        Map<Long, List<Comment>> commentsByItem = commentRepository.findByItemIdIn(itemIds)
                .stream().collect(Collectors.groupingBy(comment -> comment.getItem().getId()));

        Map<Long, List<Booking>> bookingsByItem = bookingRepository.findByItemIdInAndStatus(itemIds, BookingStatus.APPROVED, Sort.by(Sort.Direction.DESC, "start"))
                .stream().collect(Collectors.groupingBy(booking -> booking.getItem().getId()));

        LocalDateTime now = LocalDateTime.now();

        return items.stream()
                .map(item -> {
                    List<Comment> comments = commentsByItem.getOrDefault(item.getId(), Collections.emptyList());
                    ItemWithBookingsDto dto = ItemMapper.toItemWithBookingsDto(item, comments);
                    List<Booking> bookings = bookingsByItem.get(item.getId());

                    if (bookings != null) {
                        bookings.stream()
                                .filter(b -> b.getStart().isBefore(now))
                                .findFirst()
                                .ifPresent(lastBooking -> dto.setLastBooking(BookingMapper.toItemBookingDto(lastBooking)));

                        bookings.stream()
                                .filter(b -> b.getStart().isAfter(now))
                                .reduce((first, second) -> second)
                                .ifPresent(nextBooking -> dto.setNextBooking(BookingMapper.toItemBookingDto(nextBooking)));
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> search(String text) {
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        return itemRepository.search(text);
    }

    @Override
    @Transactional
    public CommentDto addComment(long userId, long itemId, CommentDto commentDto) {
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + userId + " не найден"));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с id " + itemId + " не найдена"));

        boolean hasCompletedBooking = bookingRepository
                .findByBookerIdAndItemIdAndStatusAndEndIsBefore(userId, itemId, BookingStatus.APPROVED, LocalDateTime.now())
                .stream()
                .findAny()
                .isPresent();

        if (!hasCompletedBooking) {
            throw new ValidationException("Пользователь " + userId + " не может оставить отзыв, так как у него нет завершенных аренд для вещи " + itemId);
        }

        Comment comment = new Comment();
        comment.setText(commentDto.getText());
        comment.setItem(item);
        comment.setAuthor(author);
        comment.setCreated(LocalDateTime.now());

        Comment savedComment = commentRepository.save(comment);
        return CommentMapper.toCommentDto(savedComment);
    }

    private void addBookingsToItemDto(ItemWithBookingsDto dto) {
        LocalDateTime now = LocalDateTime.now();
        bookingRepository.findFirstByItemIdAndStartBeforeAndStatus(
                        dto.getId(), now, BookingStatus.APPROVED, Sort.by(Sort.Direction.DESC, "start"))
                .ifPresent(lastBooking -> dto.setLastBooking(BookingMapper.toItemBookingDto(lastBooking)));

        bookingRepository.findFirstByItemIdAndStartAfterAndStatus(
                        dto.getId(), now, BookingStatus.APPROVED, Sort.by(Sort.Direction.ASC, "start"))
                .ifPresent(nextBooking -> dto.setNextBooking(BookingMapper.toItemBookingDto(nextBooking)));
    }
}
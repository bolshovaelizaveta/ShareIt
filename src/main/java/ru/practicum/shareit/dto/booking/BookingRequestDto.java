package ru.practicum.shareit.dto.booking;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.practicum.shareit.validation.StartBeforeEndDate;
import java.time.LocalDateTime;

@Data
@StartBeforeEndDate
public class BookingRequestDto {
    @NotNull
    private Long itemId;

    @NotNull
    @FutureOrPresent
    private LocalDateTime start;

    @NotNull
    @Future
    private LocalDateTime end;
}

package ru.practicum.shareit.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.practicum.shareit.dto.booking.BookingRequestDto;

public class StartBeforeEndDateValidator implements ConstraintValidator<StartBeforeEndDate, BookingRequestDto> {

    @Override
    public boolean isValid(BookingRequestDto bookingDto, ConstraintValidatorContext context) {
        if (bookingDto.getStart() == null || bookingDto.getEnd() == null) {
            return true;

        }
        return bookingDto.getStart().isBefore(bookingDto.getEnd());
    }
}
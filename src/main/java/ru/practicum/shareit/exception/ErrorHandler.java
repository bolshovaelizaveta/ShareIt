package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFoundException(final NotFoundException e) {
        log.warn("Ошибка 404: {}", e.getMessage());
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleConflictException(final ConflictException e) {
        log.warn("Ошибка 409: {}", e.getMessage());
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationException(final ValidationException e) {
        log.warn("Ошибка валидации 400: {}", e.getMessage());
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleOtherExceptions(final Throwable e) {
        log.error("Непредвиденная ошибка 500: {}", e.getMessage(), e);
        return Map.of("error", "Произошла непредвиденная ошибка.");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Map<String, String> handleForbiddenException(final ForbiddenException e) {
        log.warn("Ошибка доступа 403: {}", e.getMessage());
        return Map.of("error", e.getMessage());
    }
}

package ru.practicum.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class StatsErrorHandler {

    @ExceptionHandler(StatsValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public StatsApiError handleValidationException(StatsValidationException exception) {
        return new StatsApiError(
                List.of(),
                exception.getMessage(),
                "Некорректный запрос",
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now()
        );
    }
}

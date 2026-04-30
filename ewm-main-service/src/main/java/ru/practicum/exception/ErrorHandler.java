package ru.practicum.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFoundException(NotFoundException exception) {
        return new ApiError(
                List.of(),
                exception.getMessage(),
                "ID не передан или не зарегистрирован в системе",
                HttpStatus.NOT_FOUND,
                LocalDateTime.now()
        );
    }


    @ExceptionHandler(DuplicateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleDuplicateException(DuplicateException exception) {
        return new ApiError(
                List.of(),
                exception.getMessage(),
                "Ошибка",
                HttpStatus.CONFLICT,
                LocalDateTime.now()
        );
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidationException(MethodArgumentNotValidException exception) {

        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();

        boolean eventDateViolation = fieldErrors.stream()
                .anyMatch(error ->
                        "EventDateAfterTwoHours".equals(error.getCode()));

        List<String> errors = fieldErrors.stream()
                .map(error -> "Поле: " + error.getField()
                        + ". Сообщение: " + error.getDefaultMessage()
                        + ". Переданное значение: " + error.getRejectedValue())
                .toList();

        if (eventDateViolation) {
            return new ApiError(
                    List.of(),
                    exception.getMessage(),
                    "Недостаточно времени",
                    HttpStatus.CONFLICT,
                    LocalDateTime.now()
            );
        }

        return new ApiError(
                errors,
                "Некорректный запрос",
                "Ошибка валидации",
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now()
        );
    }


    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidationException(ValidationException exception) {
        return new ApiError(
                List.of(),
                exception.getMessage(),
                "Некорректный запрос",
                HttpStatus.CONFLICT,
                LocalDateTime.now()
        );
    }


    @ExceptionHandler(InitiatorRequiredException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiError handleInitiatorException(InitiatorRequiredException exception) {
        return new ApiError(
                List.of(),
                "Ошибка доступа",
                exception.getMessage(),
                HttpStatus.FORBIDDEN,
                LocalDateTime.now()
        );
    }


    @ExceptionHandler(EventModificationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handlePublishedException(EventModificationException exception) {
        return new ApiError(
                List.of(),
                "Невозможно внести изменения",
                exception.getMessage(),
                HttpStatus.CONFLICT,
                LocalDateTime.now()
        );
    }


    @ExceptionHandler(StatusChangeException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleStatusChangeException(StatusChangeException exception) {
        return new ApiError(
                List.of(),
                "Смена статуса невозможна",
                exception.getMessage(),
                HttpStatus.CONFLICT,
                LocalDateTime.now()
        );
    }


    @ExceptionHandler(EventCapacityException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleCapacityException(EventCapacityException exception) {
        return new ApiError(
                List.of(),
                "Невозможно одобрить заявку",
                exception.getMessage(),
                HttpStatus.CONFLICT,
                LocalDateTime.now()
        );
    }


    @ExceptionHandler(RequestCreationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleNotBeRequesterException(RequestCreationException exception) {
        return new ApiError(
                List.of(),
                "Ошибка создания запроса",
                exception.getMessage(),
                HttpStatus.CONFLICT,
                LocalDateTime.now()
        );
    }


    @ExceptionHandler(DeleteException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleDeleteException(DeleteException exception) {
        return new ApiError(
                List.of(),
                "Возникла ошибка при удалении",
                exception.getMessage(),
                HttpStatus.CONFLICT,
                LocalDateTime.now()
        );
    }


    @ExceptionHandler(EventStateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleEventStateException(EventStateException exception) {
        return new ApiError(
                List.of(),
                "Ошибка состояния события",
                exception.getMessage(),
                HttpStatus.CONFLICT,
                LocalDateTime.now()
        );
    }


    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleDataIntegrityViolation(DataIntegrityViolationException exception) {
        return new ApiError(
                List.of(),
                "Конфликт данных",
                exception.getMostSpecificCause().getMessage(),
                HttpStatus.CONFLICT,
                LocalDateTime.now()
        );
    }
}

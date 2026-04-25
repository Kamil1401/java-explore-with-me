package ru.practicum.exception;

public class RequestCreationException extends RuntimeException {
    public RequestCreationException(String message) {
        super(message);
    }
}

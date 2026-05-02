package ru.practicum.exception;

public class StatusChangeException extends RuntimeException {
    public StatusChangeException(String message) {
        super(message);
    }
}

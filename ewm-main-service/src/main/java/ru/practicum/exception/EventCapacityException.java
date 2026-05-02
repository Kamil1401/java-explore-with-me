package ru.practicum.exception;

public class EventCapacityException extends RuntimeException {
    public EventCapacityException(String message) {
        super(message);
    }
}

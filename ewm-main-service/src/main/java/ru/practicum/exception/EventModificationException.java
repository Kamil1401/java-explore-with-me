package ru.practicum.exception;

public class EventModificationException extends RuntimeException {
    public EventModificationException(String message) {
        super(message);
    }
}
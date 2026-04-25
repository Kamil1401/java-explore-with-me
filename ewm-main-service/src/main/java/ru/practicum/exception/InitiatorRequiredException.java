package ru.practicum.exception;

public class InitiatorRequiredException extends RuntimeException {
    public InitiatorRequiredException(String message) {
        super(message);
    }
}

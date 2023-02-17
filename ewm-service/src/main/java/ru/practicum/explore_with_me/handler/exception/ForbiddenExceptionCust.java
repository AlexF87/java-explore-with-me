package ru.practicum.explore_with_me.handler.exception;

public class ForbiddenExceptionCust extends RuntimeException {
    public ForbiddenExceptionCust(String message) {
        super(message);
    }
}

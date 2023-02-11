package ru.practicum.explore_with_me.handler.exception;

public class BadRequestException extends RuntimeException{
        public BadRequestException(String message) {
        super(message);
    }
}

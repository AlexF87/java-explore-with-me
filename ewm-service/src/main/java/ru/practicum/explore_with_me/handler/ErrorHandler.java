package ru.practicum.explore_with_me.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.explore_with_me.handler.exception.BadRequestException;

import ru.practicum.explore_with_me.handler.exception.ForbiddenExceptionCust;
import ru.practicum.explore_with_me.handler.exception.NotFoundException;
import ru.practicum.explore_with_me.handler.exception.StatsException;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFoundException(final NotFoundException e) {
        log.info("Error 404 {}", e.getMessage());
        ApiError apiError = ApiError.builder()
                .message(e.getMessage())
                .reason("For the requested operation the conditions are not met.")
                .status(HttpStatus.NOT_FOUND.name())
                .timestamp(LocalDateTime.now())
                .build();
        return apiError;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handle(final ConstraintViolationException e) {
        log.info("Error 409 {}", e.getMessage());
        ApiError apiError = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CONFLICT.name())
                .message(e.getMessage())
                .reason("Integrity constraint has been violated.")
                .build();
        return apiError;
    }

    @ExceptionHandler(ForbiddenExceptionCust.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleForbiddenException(final ForbiddenExceptionCust e) {
        log.info("Error 409 {}", e.getMessage());
        ApiError apiError = ApiError.builder()
                .message(e.getMessage())
                .reason("For the requested operation the conditions are not met.")
                .status(HttpStatus.CONFLICT.name())
                .timestamp(LocalDateTime.now())
                .build();
        return apiError;
    }

    @ExceptionHandler(StatsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleStatsException(final StatsException e) {
        log.info("Error 409 {}", e.getMessage());
        ApiError apiError = ApiError.builder()
                .message(e.getMessage())
                .reason("Sending statistics failed.")
                .status(HttpStatus.CONFLICT.name())
                .timestamp(LocalDateTime.now())
                .build();
        return apiError;
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleBadRequestException(final BadRequestException e) {
        log.info("Error 400 {}", e.getMessage());
        ApiError apiError = ApiError.builder()
                .message(e.getMessage())
                .reason("Incorrectly made request.")
                .status(HttpStatus.BAD_REQUEST.name())
                .timestamp(LocalDateTime.now())
                .build();
        return apiError;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        log.info("Error 400 {}", e.getMessage());
        ApiError apiError = ApiError.builder()
                .message(e.getMessage())
                .reason("Incorrectly made request.")
                .status(HttpStatus.BAD_REQUEST.name())
                .timestamp(LocalDateTime.now())
                .build();
        return apiError;
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMissingServletRequestParameterException(final MissingServletRequestParameterException e) {
        log.info("Error 409 {}", e.getMessage());
        ApiError apiError = ApiError.builder()
                .reason("Incorrectly made request")
                .status(HttpStatus.BAD_REQUEST.name())
                .timestamp(LocalDateTime.now())
                .message(e.getMessage())
                .build();
        return apiError;
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    protected ApiError handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        log.info("Error 409 {}", e.getMessage());
        ApiError apiError = ApiError.builder()
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CONFLICT.name())
                .reason("Integrity constraint has been violated.")
                .build();
        return apiError;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ApiError handleHttpMessageNotReadableException(final HttpMessageNotReadableException e) {
        log.info("Bad request: {}", e.getMessage());
        return ApiError.builder()
                .status(HttpStatus.BAD_REQUEST.name())
                .timestamp(LocalDateTime.now())
                .message("Required request body is missing")
                .reason("Incorrect data was sent in the request")
                .build();
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleThrowableException(final Throwable e) {
        log.info("Error 500 {}", e.getMessage());
        ApiError apiError = ApiError.builder()
                .message(e.getMessage())
                .reason("Unhandled exceptions")
                .status(HttpStatus.INTERNAL_SERVER_ERROR.name())
                .timestamp(LocalDateTime.now())
                .build();
        return apiError;
    }
}


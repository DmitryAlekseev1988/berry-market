package com.berry.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.fasterxml.jackson.core.JsonParseException;
import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
            ResourceNotFoundException ex, HttpServletRequest request) {

        ErrorResponse errorResponse =
                new ErrorResponse(LocalDateTime.now(), HttpStatus.NOT_FOUND.value(), "Не найдено",
                        ex.getMessage(), request.getRequestURI());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException ex,
            HttpServletRequest request) {

        ErrorResponse errorResponse =
                new ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(),
                        "Неверный запрос", ex.getMessage(), request.getRequestURI());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException ex,
            HttpServletRequest request) {

        ErrorResponse errorResponse =
                new ErrorResponse(LocalDateTime.now(), HttpStatus.FORBIDDEN.value(),
                        "Доступ запрещён", ex.getMessage(), request.getRequestURI());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException ex,
            HttpServletRequest request) {

        ErrorResponse errorResponse =
                new ErrorResponse(LocalDateTime.now(), HttpStatus.UNAUTHORIZED.value(),
                        "Не авторизован", "Неверный email или пароль", request.getRequestURI());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        Map<String, String> validationErrors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            validationErrors.put(fieldName, errorMessage);
        });

        ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(), "Ошибка валидации", "Неверные входные данные",
                request.getRequestURI(), validationErrors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex, HttpServletRequest request) {

        String message = "Неверный формат JSON";
        if (ex.getCause() instanceof JsonParseException) {
            message = "Ошибка парсинга JSON: " + ex.getCause().getMessage();
        }

        ErrorResponse errorResponse =
                new ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(),
                        "Неверный запрос", message, request.getRequestURI());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex,
            HttpServletRequest request) {

        ErrorResponse errorResponse =
                new ErrorResponse(LocalDateTime.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "Внутренняя ошибка сервера", ex.getMessage(), request.getRequestURI());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}

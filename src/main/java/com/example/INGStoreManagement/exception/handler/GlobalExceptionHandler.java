package com.example.INGStoreManagement.exception.handler;

import com.example.INGStoreManagement.exception.ProductAlreadyExistsException;
import com.example.INGStoreManagement.exception.ProductNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    public static final String NOT_FOUND_TITLE = "Product not found";
    public static final String ALREADY_EXISTS_TITLE = "Product not found";
    public static final String VALIDATION_FAILED_TITLE = "Validation failed";
    public static final String INTERNAL_SERVER_ERROR_TITLE = "Internal server error";

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorDto> handleProductNotFoundException(ProductNotFoundException ex, WebRequest request) {
        ErrorDto errorDto = ErrorDto.builder()
                .title(NOT_FOUND_TITLE)
                .status(HttpStatus.NOT_FOUND)
                .detail(ex.getLocalizedMessage())
                .path(request.getDescription(false).replace("uri=", ""))
                .timestamp(LocalDateTime.now())
                .fields(new HashMap<>())
                .build();

        log.error(NOT_FOUND_TITLE + " {}", errorDto, ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDto);
    }

    @ExceptionHandler(ProductAlreadyExistsException.class)
    public ResponseEntity<ErrorDto> handleProductAlreadyExistsException(ProductAlreadyExistsException ex, WebRequest request) {
        ErrorDto errorDto = ErrorDto.builder()
                .title(ALREADY_EXISTS_TITLE)
                .status(HttpStatus.CONFLICT)
                .detail(ex.getLocalizedMessage())
                .path(request.getDescription(false).replace("uri=", ""))
                .timestamp(LocalDateTime.now())
                .fields(new HashMap<>())
                .build();

        log.error(ALREADY_EXISTS_TITLE + " {}", errorDto, ex);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorDto);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, String> errorsPerFields = ex
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));

        ErrorDto errorDto = ErrorDto.builder()
                .title(VALIDATION_FAILED_TITLE)
                .status(HttpStatus.BAD_REQUEST)
                .detail(ex.getLocalizedMessage())
                .path(request.getDescription(false).replace("uri=", ""))
                .timestamp(LocalDateTime.now())
                .fields(errorsPerFields)
                .build();

        log.error(VALIDATION_FAILED_TITLE + " {}", errorDto, ex);
        return ResponseEntity.badRequest().body(errorDto);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleAllExceptions(Exception ex, WebRequest request) {
        ErrorDto errorDto = ErrorDto.builder()
                .title(INTERNAL_SERVER_ERROR_TITLE)
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .detail(ex.getLocalizedMessage())
                .path(request.getDescription(false).replace("uri=", ""))
                .timestamp(LocalDateTime.now())
                .fields(new HashMap<>())
                .build();

        log.error(INTERNAL_SERVER_ERROR_TITLE + " {}", errorDto, ex);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorDto);
    }
}

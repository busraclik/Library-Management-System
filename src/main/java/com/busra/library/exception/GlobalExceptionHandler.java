package com.busra.library.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {

        log.warn("Validation error: {}", ex.getMessage(), ex);

        List<String> details = new ArrayList<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String message = error.getField() + ": " + error.getDefaultMessage();
            details.add(message);
        });

        ErrorResponse errorResponse = new ErrorResponse("Validation failed.", details);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFound(UserNotFoundException ex) {

        log.error("User not found: {}", ex.getMessage(), ex);

        List<String> detail = new ArrayList<>();
        detail.add(ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse("User not found.", detail);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<?> handleBookNotFound(BookNotFoundException ex) {
        log.error("Book not found: {}", ex.getMessage(), ex);
        List<String> detail = new ArrayList<>();
        detail.add(ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse("Book not found.", detail);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BorrowRestrictionException.class)
    public ResponseEntity<?> handleBorrowRestrictionException(BorrowRestrictionException ex) {

        log.warn("Borrow restriction: {}", ex.getMessage(), ex);

        List<String> detail = new ArrayList<>();
        detail.add(ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse("Borrow restriction.", detail);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<?> handleUsernameAlreadyExistsException(UsernameAlreadyExistsException ex) {
        log.error("Username already exists: {}", ex.getMessage(), ex);
        List<String> detail = new ArrayList<>();
        detail.add(ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse("Username already exists.", detail);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }


}

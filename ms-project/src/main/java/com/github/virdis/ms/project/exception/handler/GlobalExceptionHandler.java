package com.github.virdis.ms.project.exception.handler;

import com.github.virdis.ms.project.exception.DatabaseException;
import com.github.virdis.ms.project.exception.InvalidStatusTransitionException;
import com.github.virdis.ms.project.exception.ResourceNotFoundException;
import com.github.virdis.ms.project.exception.SpaceServiceUnavailableException;
import com.github.virdis.ms.project.exception.dto.CustomErrorDTO;
import com.github.virdis.ms.project.exception.dto.ValidationErrorDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<CustomErrorDTO> handleResourceNotFound(
            ResourceNotFoundException e,
            HttpServletRequest request
    ) {

        HttpStatus status = HttpStatus.NOT_FOUND;

        CustomErrorDTO error = new CustomErrorDTO(
                Instant.now(),
                status.value(),
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(status)
                .body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomErrorDTO> methodArgumentNotValid(
            MethodArgumentNotValidException e,
            HttpServletRequest request
    ) {

        HttpStatus status = HttpStatus.UNPROCESSABLE_CONTENT;

        ValidationErrorDTO error = new ValidationErrorDTO(
                Instant.now(),
                status.value(),
                "Dados inválidos",
                request.getRequestURI()
        );

        for (FieldError fieldError :
                e.getBindingResult().getFieldErrors()) {

            error.addError(
                    fieldError.getField(),
                    fieldError.getDefaultMessage()
            );
        }

        return ResponseEntity
                .status(status)
                .body(error);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<CustomErrorDTO> handleHttpMessageNotReadable(
            HttpMessageNotReadableException e,
            HttpServletRequest request
    ) {

        HttpStatus status = HttpStatus.BAD_REQUEST;

        CustomErrorDTO error = new CustomErrorDTO(
                Instant.now(),
                status.value(),
                "Requisição inválida "
                        + "(JSON malformado ou corpo não interpretável).",
                request.getRequestURI()
        );

        return ResponseEntity
                .status(status)
                .body(error);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<CustomErrorDTO> handleTypeMismatch(
            MethodArgumentTypeMismatchException e,
            HttpServletRequest request
    ) {

        HttpStatus status = HttpStatus.BAD_REQUEST;

        CustomErrorDTO error = new CustomErrorDTO(
                Instant.now(),
                status.value(),
                "Requisição inválida "
                        + "(parâmetro com tipo ou formato incorreto).",
                request.getRequestURI()
        );

        return ResponseEntity
                .status(status)
                .body(error);
    }

    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<CustomErrorDTO> handleDatabase(
            DatabaseException e,
            HttpServletRequest request
    ) {

        HttpStatus status = HttpStatus.CONFLICT;

        CustomErrorDTO error = new CustomErrorDTO(
                Instant.now(),
                status.value(),
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(status)
                .body(error);
    }

    @ExceptionHandler(InvalidStatusTransitionException.class)
    public ResponseEntity<CustomErrorDTO> handleInvalidStatusTransition(
            InvalidStatusTransitionException e,
            HttpServletRequest request
    ) {

        HttpStatus status = HttpStatus.CONFLICT;

        CustomErrorDTO error = new CustomErrorDTO(
                Instant.now(),
                status.value(),
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(status)
                .body(error);
    }

    @ExceptionHandler(SpaceServiceUnavailableException.class)
    public ResponseEntity<CustomErrorDTO> handleSpaceServiceUnavailable(
            SpaceServiceUnavailableException e,
            HttpServletRequest request
    ) {

        HttpStatus status = HttpStatus.SERVICE_UNAVAILABLE;

        CustomErrorDTO error = new CustomErrorDTO(
                Instant.now(),
                status.value(),
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(status)
                .body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomErrorDTO> handleGenericException(
            Exception e,
            HttpServletRequest request
    ) {

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        CustomErrorDTO error = new CustomErrorDTO(
                Instant.now(),
                status.value(),
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(status)
                .body(error);
    }
}
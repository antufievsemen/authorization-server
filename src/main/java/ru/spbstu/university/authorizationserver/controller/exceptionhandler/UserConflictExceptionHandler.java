package ru.spbstu.university.authorizationserver.controller.exceptionhandler;

import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.spbstu.university.authorizationserver.controller.exceptionhandler.dto.ErrorBody;
import ru.spbstu.university.authorizationserver.service.exception.UserConflictException;

@ControllerAdvice
public class UserConflictExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<ErrorBody> handleException(@NonNull UserConflictException exception) {
        return new ResponseEntity<>(new ErrorBody(HttpStatus.CONFLICT, exception.getMessage()), HttpStatus.CONFLICT);
    }
}

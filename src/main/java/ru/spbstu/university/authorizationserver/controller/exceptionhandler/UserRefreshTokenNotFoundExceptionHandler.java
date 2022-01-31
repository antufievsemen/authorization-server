package ru.spbstu.university.authorizationserver.controller.exceptionhandler;

import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.spbstu.university.authorizationserver.controller.exceptionhandler.dto.ErrorBody;
import ru.spbstu.university.authorizationserver.service.exception.UserRefreshTokenNotFoundException;

@ControllerAdvice
public class UserRefreshTokenNotFoundExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<ErrorBody> handleException(@NonNull UserRefreshTokenNotFoundException exception) {
        return new ResponseEntity<>(new ErrorBody(HttpStatus.NOT_FOUND, exception.getMessage()), HttpStatus.NOT_FOUND);
    }

}

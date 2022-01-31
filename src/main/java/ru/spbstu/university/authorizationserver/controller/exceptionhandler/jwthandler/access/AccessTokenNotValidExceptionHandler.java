package ru.spbstu.university.authorizationserver.controller.exceptionhandler.jwthandler.access;

import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.spbstu.university.authorizationserver.controller.exceptionhandler.jwthandler.dto.JwtErrorBody;
import ru.spbstu.university.authorizationserver.service.encyption.access.exception.AccessTokenNotValidException;

@ControllerAdvice
public class AccessTokenNotValidExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<JwtErrorBody> handleException(@NonNull AccessTokenNotValidException exception) {
        return new ResponseEntity<>(new JwtErrorBody(HttpStatus.BAD_REQUEST,
                exception.getMessage()), HttpStatus.BAD_REQUEST);
    }
}

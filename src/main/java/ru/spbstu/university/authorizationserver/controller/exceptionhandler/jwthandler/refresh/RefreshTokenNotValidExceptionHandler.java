package ru.spbstu.university.authorizationserver.controller.exceptionhandler.jwthandler.refresh;

import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.spbstu.university.authorizationserver.controller.exceptionhandler.jwthandler.dto.JwtErrorBody;
import ru.spbstu.university.authorizationserver.service.encyption.refresh.exception.RefreshTokenNotValidException;

@ControllerAdvice
public class RefreshTokenNotValidExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<JwtErrorBody> handleException(@NonNull RefreshTokenNotValidException exception) {
        return new ResponseEntity<>(new JwtErrorBody(HttpStatus.BAD_REQUEST,
                exception.getMessage()), HttpStatus.BAD_REQUEST);
    }
}

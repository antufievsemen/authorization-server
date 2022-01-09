package ru.spbstu.university.authorizationserver.controller.exceptionhandler;

import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.spbstu.university.authorizationserver.controller.exceptionhandler.dto.ErrorBody;
import ru.spbstu.university.authorizationserver.service.exception.ClientNotUniqueException;

@ControllerAdvice
public class ClientNotUniqueExceptionHandler {

    @ExceptionHandler(ClientNotUniqueException.class)
    public ResponseEntity<ErrorBody> handleException(@NonNull ClientNotUniqueException e) {
        return new ResponseEntity<>(new ErrorBody(HttpStatus.CONFLICT, e.getMessage()), HttpStatus.CONFLICT);
    }
}

package ru.spbstu.university.authorizationserver.service.exception;

public class CallbackNotValidException extends RuntimeException {
    public CallbackNotValidException() {
        super("Redirect uri not valid");
    }
}

package ru.spbstu.university.authorizationserver.service.auth.exception;

public class BadRedirectRequestException extends RuntimeException {
    public BadRedirectRequestException() {
        super("Redirect is not correct");
    }
}

package ru.spbstu.university.authorizationserver.service.auth.exception;

public class LogoutErrorUserNotIdentifierException extends RuntimeException{
    public LogoutErrorUserNotIdentifierException() {
        super("Logout is incorrect");
    }
}

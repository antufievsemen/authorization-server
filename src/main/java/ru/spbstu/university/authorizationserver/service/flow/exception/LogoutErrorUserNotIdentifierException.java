package ru.spbstu.university.authorizationserver.service.flow.exception;

public class LogoutErrorUserNotIdentifierException extends RuntimeException{
    public LogoutErrorUserNotIdentifierException() {
        super("Logout is incorrect");
    }
}

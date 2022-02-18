package ru.spbstu.university.authorizationserver.service.auth.exception;

public class LoginAcceptException extends RuntimeException{
    public LoginAcceptException() {
        super("Login accept ");
    }
}

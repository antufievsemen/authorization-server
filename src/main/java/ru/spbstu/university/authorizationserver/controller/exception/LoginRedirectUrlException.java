package ru.spbstu.university.authorizationserver.controller.exception;

public class LoginRedirectUrlException extends RuntimeException {
    public LoginRedirectUrlException() {
        super("Login redirect url is incorrect");
    }
}

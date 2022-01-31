package ru.spbstu.university.authorizationserver.service.exception;

public class LoginChallengeNotValidException extends RuntimeException{
    public LoginChallengeNotValidException() {
        super("Login challenge is not valid");
    }
}

package ru.spbstu.university.authorizationserver.service.auth.security.pkce.exception;

public class CodeChallengeMethodIncorrectException extends RuntimeException {
    public CodeChallengeMethodIncorrectException() {
        super("Method is not allowed");
    }
}

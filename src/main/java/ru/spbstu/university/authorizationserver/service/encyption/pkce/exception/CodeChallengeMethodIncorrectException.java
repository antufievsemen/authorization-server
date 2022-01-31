package ru.spbstu.university.authorizationserver.service.encyption.pkce.exception;

public class CodeChallengeMethodIncorrectException extends RuntimeException {
    public CodeChallengeMethodIncorrectException() {
        super("Method is not allowed");
    }
}

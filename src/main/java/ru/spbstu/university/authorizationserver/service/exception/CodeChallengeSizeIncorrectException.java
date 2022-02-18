package ru.spbstu.university.authorizationserver.service.exception;

public class CodeChallengeSizeIncorrectException extends RuntimeException {
    public CodeChallengeSizeIncorrectException() {
        super("Code challenge incorrect");
    }
}

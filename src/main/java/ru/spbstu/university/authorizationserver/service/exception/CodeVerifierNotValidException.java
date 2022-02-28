package ru.spbstu.university.authorizationserver.service.exception;

public class CodeVerifierNotValidException extends RuntimeException {
    public CodeVerifierNotValidException() {
        super("Code verifier is incorrect");
    }
}

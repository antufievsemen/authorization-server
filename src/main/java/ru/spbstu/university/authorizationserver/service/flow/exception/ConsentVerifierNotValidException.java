package ru.spbstu.university.authorizationserver.service.flow.exception;

public class ConsentVerifierNotValidException extends RuntimeException{
    public ConsentVerifierNotValidException() {
        super("Consent verifier is incorrect or expired");
    }
}

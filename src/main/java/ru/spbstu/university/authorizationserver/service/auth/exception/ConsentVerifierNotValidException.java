package ru.spbstu.university.authorizationserver.service.auth.exception;

public class ConsentVerifierNotValidException extends RuntimeException{
    public ConsentVerifierNotValidException() {
        super("Consent verifier is incorrect or expired");
    }
}

package ru.spbstu.university.authorizationserver.service.exception;

public class ConsentChallengeNotValidException extends RuntimeException{
    public ConsentChallengeNotValidException() {
        super("Consent challenge is expited or not valid");
    }
}

package ru.spbstu.university.authorizationserver.service.exception;

public class JwksNotFoundException extends RuntimeException{
    public JwksNotFoundException() {
        super("Jwks not found");
    }
}

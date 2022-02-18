package ru.spbstu.university.authorizationserver.service.auth.exception;

public class TokenRevokeException extends RuntimeException {
    public TokenRevokeException() {
        super("Token is revoke");
    }
}

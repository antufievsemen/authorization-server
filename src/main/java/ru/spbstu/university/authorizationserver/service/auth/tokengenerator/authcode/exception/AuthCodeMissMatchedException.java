package ru.spbstu.university.authorizationserver.service.auth.tokengenerator.authcode.exception;

public class AuthCodeMissMatchedException extends RuntimeException {
    public AuthCodeMissMatchedException() {
        super("Auth code miss matched");
    }
}

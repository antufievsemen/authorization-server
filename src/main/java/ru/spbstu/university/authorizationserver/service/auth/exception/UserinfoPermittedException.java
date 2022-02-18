package ru.spbstu.university.authorizationserver.service.auth.exception;

public class UserinfoPermittedException extends RuntimeException {
    public UserinfoPermittedException() {
        super("User info not permitted");
    }
}

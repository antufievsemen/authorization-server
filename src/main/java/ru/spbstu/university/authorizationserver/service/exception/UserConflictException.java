package ru.spbstu.university.authorizationserver.service.exception;

public class UserConflictException extends RuntimeException {
    public UserConflictException() {
        super("User already exist");
    }
}

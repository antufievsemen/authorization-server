package ru.spbstu.university.authorizationserver.service.exception;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException() {
        super("End user not found");
    }
}

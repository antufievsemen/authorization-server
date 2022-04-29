package ru.spbstu.university.authorizationserver.service.auth.exception;

public class UserInfoNotFoundException extends RuntimeException{
    public UserInfoNotFoundException() {
        super("User info not found");
    }
}

package ru.spbstu.university.authorizationserver.service.auth.exception;

public class UserinfoMissMatchedException extends RuntimeException {
    public UserinfoMissMatchedException() {
        super("Userinfo miss matched");
    }
}

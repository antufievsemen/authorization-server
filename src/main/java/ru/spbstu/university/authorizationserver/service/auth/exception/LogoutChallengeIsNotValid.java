package ru.spbstu.university.authorizationserver.service.auth.exception;

public class LogoutChallengeIsNotValid extends RuntimeException{
    public LogoutChallengeIsNotValid() {
        super("Logout challenge is not valid");
    }
}

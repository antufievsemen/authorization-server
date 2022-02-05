package ru.spbstu.university.authorizationserver.service.flow.exception;

public class LogoutChallengeIsNotValid extends RuntimeException{
    public LogoutChallengeIsNotValid() {
        super("Logout challenge is not valid");
    }
}

package ru.spbstu.university.authorizationserver.service.exception;

public class ClientNotFoundException extends RuntimeException{

    public ClientNotFoundException() {
        super("Client not found");
    }
}

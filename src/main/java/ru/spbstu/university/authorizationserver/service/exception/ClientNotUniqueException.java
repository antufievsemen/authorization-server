package ru.spbstu.university.authorizationserver.service.exception;

public class ClientNotUniqueException extends RuntimeException{
    public ClientNotUniqueException() {
        super("Client already exist");
    }
}

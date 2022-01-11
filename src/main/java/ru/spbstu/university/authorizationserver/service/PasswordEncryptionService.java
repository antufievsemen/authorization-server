package ru.spbstu.university.authorizationserver.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PasswordEncryptionService {

    @NonNull
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @NonNull
    public String encryptPassword(@NonNull String password) {
        return bCryptPasswordEncoder.encode(password);
    }
}

package ru.spbstu.university.authorizationserver.controller.flow.authcode;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.spbstu.university.authorizationserver.controller.flow.authcode.dto.request.UserCredentialsRequest;
import ru.spbstu.university.authorizationserver.controller.flow.authcode.dto.response.ResponseInit;
import ru.spbstu.university.authorizationserver.service.UserService;

@RestController
@AllArgsConstructor
public class AuthCodeController {

    @NonNull
    private final UserService userService;

}

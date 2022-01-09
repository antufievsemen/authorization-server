//package ru.spbstu.university.authorizationserver.controller.crud;
//
//import lombok.AllArgsConstructor;
//import lombok.NonNull;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.ResponseStatus;
//import org.springframework.web.bind.annotation.RestController;
//import ru.spbstu.university.authorizationserver.controller.annotation.ServerName;
//import ru.spbstu.university.authorizationserver.controller.crud.dto.request.TokenRequest;
//import ru.spbstu.university.authorizationserver.controller.crud.dto.response.TokenResponse;
//
//@ServerName
//@RestController
//@AllArgsConstructor
//public class TokenController {
//
//    @ResponseStatus(HttpStatus.OK)
//    @PostMapping("/token/introspecting")
//    public TokenResponse decode(@NonNull @RequestBody TokenRequest tokenRequest) {
//
//    }
//
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    @DeleteMapping("/token/revoking")
//    public void revoke(@NonNull @RequestBody TokenRequest tokenRequest) {
//
//    }
//}

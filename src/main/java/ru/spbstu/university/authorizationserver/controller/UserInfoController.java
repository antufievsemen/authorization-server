//package ru.spbstu.university.authorizationserver.controller;
//
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NonNull;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseStatus;
//import org.springframework.web.bind.annotation.RestController;
//import ru.spbstu.university.authorizationserver.controller.annotation.ServerName;
//import ru.spbstu.university.authorizationserver.model.enums.Gender;
//
//@ServerName
//@RestController
//@AllArgsConstructor
//public class UserInfoController {
//
//    @ResponseStatus(HttpStatus.OK)
//    @GetMapping("/userinfo")
//    public UserInfoResponse userinfo(@RequestParam(name = "openid_token") String openidToken) {
//        return new UserInfoResponse();
//    }
//
//    @Getter
//    @AllArgsConstructor
//    private static class UserInfoResponse {
//        @NonNull
//        private final String sub;
//        @NonNull
//        private final String firstname;
//        @NonNull
//        private final String lastname;
//        @NonNull
//        private final Gender gender;
//        @NonNull
//        private final String phoneNumber;
//        private final boolean phoneVerified;
//        @NonNull
//        private final String email;
//        private final boolean emailVerified;
//    }
//}

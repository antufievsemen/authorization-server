package ru.spbstu.university.authorizationserver.controller;

import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.spbstu.university.authorizationserver.controller.annotation.ApiV1;
import ru.spbstu.university.authorizationserver.controller.dto.response.JwksOpenIdResponse;
import ru.spbstu.university.authorizationserver.controller.dto.response.JwksResponse;
import ru.spbstu.university.authorizationserver.service.auth.security.JwksService;

@ApiV1
@RestController
@AllArgsConstructor
public class JwksController {
    @NonNull
    private final JwksService jwksService;

//    @ResponseStatus
//    @GetMapping("/.well-known/openid-configuration")
//    public JwksOpenIdResponse openidConfiguration() {
//
//    }

    @ResponseStatus
    @GetMapping("/.well-known/jwks.json")
    public JwksResponse keys() {
        return toResponse(jwksService.getAll());
    }

    @NonNull
    private JwksResponse toResponse(@NonNull List<JwksService.KeyDto> keyDtoList) {
        return new JwksResponse(keyDtoList.stream()
                .map(dto -> new JwksResponse.Key("RS256", "AQAB", dto.getPublicKey(), dto.getId(),
                        dto.getAlg(), "sign"))
                .collect(Collectors.toList()));
    }
}

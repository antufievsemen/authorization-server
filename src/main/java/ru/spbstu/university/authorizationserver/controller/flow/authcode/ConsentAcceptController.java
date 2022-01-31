package ru.spbstu.university.authorizationserver.controller.flow.authcode;

import javax.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.spbstu.university.authorizationserver.controller.flow.authcode.dto.response.ConsentAcceptResponse;
import ru.spbstu.university.authorizationserver.controller.flow.authcode.dto.response.ConsentInfoResponse;
import ru.spbstu.university.authorizationserver.controller.flow.authcode.dto.response.LoginAcceptResponse;
import ru.spbstu.university.authorizationserver.model.RequestParams;
import ru.spbstu.university.authorizationserver.service.ConsentAcceptService;

@RestController
@AllArgsConstructor
public class ConsentAcceptController {

    @NonNull
    private final ConsentAcceptService consentAcceptService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/auth/requests/consent")
    public ConsentInfoResponse get(@RequestParam(name = "consent_challenge") String challenge,
                                   @NonNull HttpSession httpSession) {

        return toResponse(consentAcceptService.getInfo(challenge, httpSession.getId()));
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/auth/requests/consent/accept")
    public ConsentAcceptResponse accept(@RequestParam(name = "consent_challenge") String challenge,
                                        @NonNull HttpSession httpSession) {

        return toAcceptResponse(consentAcceptService.accept(challenge, httpSession.getId()));
    }

    @NonNull
    private ConsentInfoResponse toResponse(@NonNull ConsentAcceptService.ConsentInfo consentInfo) {
        return new ConsentInfoResponse();
    }

    @NonNull
    private ConsentAcceptResponse toAcceptResponse(@NonNull RequestParams requestParams) {
        return new ConsentAcceptResponse(new DefaultUriBuilderFactory("http://localhost:8080/v1/auth-server/auth").builder()
                .queryParams(requestParams.getMap()).build().toASCIIString());
    }
}

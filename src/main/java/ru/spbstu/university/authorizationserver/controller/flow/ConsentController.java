package ru.spbstu.university.authorizationserver.controller.flow;

import javax.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.spbstu.university.authorizationserver.controller.annotation.ServerName;
import ru.spbstu.university.authorizationserver.controller.flow.dto.request.AcceptConsentRequest;
import ru.spbstu.university.authorizationserver.controller.flow.dto.response.ConsentAcceptResponse;
import ru.spbstu.university.authorizationserver.controller.flow.dto.response.ConsentInfoResponse;
import ru.spbstu.university.authorizationserver.service.ConsentService;

@ServerName
@RestController
@AllArgsConstructor
public class ConsentController {

    @NonNull
    private final ConsentService consentService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/oauth2/auth/requests/consent")
    public ConsentInfoResponse get(@RequestParam(name = "consent_challenge") String challenge,
                                   @NonNull HttpSession httpSession) {
        return toResponse(consentService.getInfo(challenge, httpSession.getId()));
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/oauth2/auth/requests/consent/accept")
    public ConsentAcceptResponse accept(@RequestParam(name = "consent_challenge") String challenge,
                                        @RequestBody AcceptConsentRequest request) {
        return toAcceptResponse(consentService.accept(challenge, request.getGrantedScopes(), request.getGrantedAccessTokenAudience()));
    }

    @NonNull
    private ConsentInfoResponse toResponse(@NonNull ConsentService.ConsentInfo consentInfo) {
        return new ConsentInfoResponse(consentInfo.getSubject(), consentInfo.getRequestedResponseType(),
                consentInfo.getRequestedScopes(), consentInfo.getRequestedUrl(), consentInfo.getClientInfo());
    }

    @NonNull
    private ConsentAcceptResponse toAcceptResponse(@NonNull MultiValueMap<String, String> map) {
        return new ConsentAcceptResponse(new DefaultUriBuilderFactory("http://localhost:8080/v1/auth-server/auth").builder()
                .queryParams(map).build().toASCIIString());
    }
}

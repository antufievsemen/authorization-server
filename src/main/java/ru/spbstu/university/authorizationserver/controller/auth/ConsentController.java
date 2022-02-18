package ru.spbstu.university.authorizationserver.controller.auth;

import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.spbstu.university.authorizationserver.config.SelfIssuerSettings;
import ru.spbstu.university.authorizationserver.controller.annotation.ApiV1;
import ru.spbstu.university.authorizationserver.controller.auth.dto.request.AcceptConsentRequest;
import ru.spbstu.university.authorizationserver.controller.auth.dto.response.ConsentAcceptResponse;
import ru.spbstu.university.authorizationserver.controller.auth.dto.response.ConsentInfoResponse;
import ru.spbstu.university.authorizationserver.model.enums.ResponseTypeEnum;
import ru.spbstu.university.authorizationserver.service.auth.FlowSessionManagerService;
import ru.spbstu.university.authorizationserver.service.auth.dto.loginconsent.ConsentAccept;
import ru.spbstu.university.authorizationserver.service.auth.dto.loginconsent.ConsentInfo;

@ApiV1
@RestController
@AllArgsConstructor
public class ConsentController {
    @NonNull
    private final FlowSessionManagerService flowSessionManagerService;
    @NonNull
    private final SelfIssuerSettings settings;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/oauth2/auth/requests/consent")
    public ConsentInfoResponse get(@RequestParam(name = "consent_verifier") String consentVerifier) {
        return getConsentInfoResponse(flowSessionManagerService.getConsentInfo(consentVerifier));
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/oauth2/auth/requests/consent/accept")
    public ConsentAcceptResponse accept(@RequestParam(name = "consent_verifier") String consentVerifier,
                                        @RequestBody AcceptConsentRequest request) {
        return getConsentAcceptResponse(flowSessionManagerService.acceptConsent(consentVerifier,
                request.getPermittedScopes(), request.getPermittedAudience(), request.getUserinfo()));
    }

    @NonNull
    private ConsentInfoResponse getConsentInfoResponse(@NonNull ConsentInfo consentInfo) {
        return new ConsentInfoResponse(consentInfo.getSubject(), consentInfo.getAuthClient(),
                consentInfo.getResponseTypes().stream().map(ResponseTypeEnum::getName).collect(Collectors.toList()),
                consentInfo.getScopes(), consentInfo.getRedirectUri(), consentInfo.getGrantTypes());
    }

    @NonNull
    private ConsentAcceptResponse getConsentAcceptResponse(@NonNull ConsentAccept consentAccept) {
        return new ConsentAcceptResponse(new DefaultUriBuilderFactory(settings.getIssuer())
                .uriString("/v1/oauth2/auth")
                .queryParams(consentAccept.getAttributes())
                .build().toASCIIString());
    }
}

package ru.spbstu.university.authorizationserver.controller.flow;

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
import ru.spbstu.university.authorizationserver.controller.flow.dto.request.ConsentRequest;
import ru.spbstu.university.authorizationserver.controller.flow.dto.response.ConsentRedirectResponse;
import ru.spbstu.university.authorizationserver.controller.flow.dto.response.ConsentInfoResponse;
import ru.spbstu.university.authorizationserver.model.enums.ResponseTypeEnum;
import ru.spbstu.university.authorizationserver.service.auth.FlowSessionManager;
import ru.spbstu.university.authorizationserver.service.auth.dto.loginconsent.ConsentAccept;
import ru.spbstu.university.authorizationserver.service.auth.dto.loginconsent.ConsentInfo;

@ApiV1
@RestController
@AllArgsConstructor
public class ConsentController {
    @NonNull
    private final FlowSessionManager flowSessionManager;
    @NonNull
    private final SelfIssuerSettings settings;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/oauth2/auth/requests/consent")
    public ConsentInfoResponse get(@RequestParam(name = "consent_verifier") String consentVerifier) {
        return getConsentInfoResponse(flowSessionManager.getConsentInfo(consentVerifier));
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/oauth2/auth/requests/consent/accept")
    public ConsentRedirectResponse accept(@RequestParam(name = "consent_verifier") String consentVerifier,
                                          @RequestBody ConsentRequest request) {
        return getConsentAcceptResponse(flowSessionManager.acceptConsent(consentVerifier,
                request.getPermittedScopes(), request.getPermittedAudience(), request.getUserinfo()));
    }

    @NonNull
    private ConsentInfoResponse getConsentInfoResponse(@NonNull ConsentInfo consentInfo) {
        return new ConsentInfoResponse(consentInfo.getSubject(), consentInfo.getClientInfo(),
                consentInfo.getResponseTypes().stream().map(ResponseTypeEnum::getName).collect(Collectors.toList()),
                consentInfo.getScopes(), consentInfo.getRedirectUri(), consentInfo.getGrantTypes());
    }

    @NonNull
    private ConsentRedirectResponse getConsentAcceptResponse(@NonNull ConsentAccept consentAccept) {
        return new ConsentRedirectResponse(new DefaultUriBuilderFactory(settings.getIssuer())
                .uriString("/v1/oauth2/auth")
                .queryParams(consentAccept.getAttributes())
                .build().toASCIIString());
    }
}

package ru.spbstu.university.authorizationserver.service.auth;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import ru.spbstu.university.authorizationserver.model.AuthParams;
import ru.spbstu.university.authorizationserver.model.User;
import ru.spbstu.university.authorizationserver.model.enums.ScopeEnum;
import ru.spbstu.university.authorizationserver.model.ConsentParams;
import ru.spbstu.university.authorizationserver.service.ClientService;
import ru.spbstu.university.authorizationserver.service.ConsentParamsService;
import ru.spbstu.university.authorizationserver.service.AuthParamsService;
import ru.spbstu.university.authorizationserver.service.ScopeService;
import ru.spbstu.university.authorizationserver.service.UserService;
import ru.spbstu.university.authorizationserver.service.auth.dto.loginconsent.ConsentAccept;
import ru.spbstu.university.authorizationserver.service.auth.dto.loginconsent.ConsentInfo;
import ru.spbstu.university.authorizationserver.service.auth.dto.loginconsent.LoginAccept;
import ru.spbstu.university.authorizationserver.service.auth.dto.loginconsent.LoginInfo;
import ru.spbstu.university.authorizationserver.service.auth.dto.logout.LogoutAccept;
import ru.spbstu.university.authorizationserver.service.auth.dto.logout.LogoutInfo;
import ru.spbstu.university.authorizationserver.service.auth.dto.redirect.enums.RedirectResponseEnum;
import ru.spbstu.university.authorizationserver.service.auth.exception.LoginAcceptException;
import ru.spbstu.university.authorizationserver.service.auth.exception.SessionExpiredException;
import ru.spbstu.university.authorizationserver.service.auth.exception.UserinfoMissMatchedException;
import ru.spbstu.university.authorizationserver.service.auth.security.codeverifier.CodeVerifierProvider;

@Service
@AllArgsConstructor
public class FlowSessionManager {
    @NonNull
    private final AuthParamsService authParamsService;
    @NonNull
    private final UserService userService;
    @NonNull
    private final ClientService clientService;
    @NonNull
    private final ScopeService scopeService;
    @NonNull
    private final CodeVerifierProvider codeVerifierProvider;
    @NonNull
    private final ConsentParamsService consentParamsService;
    @NonNull
    private final LogoutInfoService logoutInfoService;

    @NonNull
    public LoginInfo getLoginInfo(@NonNull String loginVerifier) {
        codeVerifierProvider.validate(loginVerifier);
        final AuthParams authParams = authParamsService.get(loginVerifier)
                .orElseThrow(SessionExpiredException::new);

        return new LoginInfo(authParams.getClientInfo(), authParams.getResponseTypes(),
                authParams.getScopes(), authParams.getGrantTypes(), authParams.getRedirectUri());
    }

    @NonNull
    public LoginAccept acceptLogin(@NonNull String loginVerifier, @NonNull String subject) {
        codeVerifierProvider.validate(loginVerifier);
        final AuthParams authParams = authParamsService.get(loginVerifier)
                .orElseThrow(LoginAcceptException::new);
        authParamsService.delete(loginVerifier);


        final User user = userService.create(subject,
                clientService.getByClientId(authParams.getClientInfo().getClientId()),
                scopeService.getAllByName(authParams.getScopes()), authParams.getSessionId());

        authParams.setSubject(user.getId());
        authParamsService.create(loginVerifier, authParams);
        return new LoginAccept(loginVerifier);
    }

    @NonNull
    public ConsentInfo getConsentInfo(@NonNull String consentVerifier) {
        codeVerifierProvider.validate(consentVerifier);
        final ConsentParams consentParams = consentParamsService.get(consentVerifier)
                .orElseThrow(SessionExpiredException::new);

        final AuthParams authParams = consentParams.getAuthParams();

        return new ConsentInfo(Objects.requireNonNull(authParams.getSubject()), authParams.getClientInfo(),
                authParams.getGrantTypes(), authParams.getResponseTypes(), authParams.getScopes(),
                authParams.getRedirectUri());
    }

    @NonNull
    public ConsentAccept acceptConsent(@NonNull String consentVerifier,
                                       @NonNull List<String> permittedScopes, @NonNull List<String> aud,
                                       @Nullable Map<String, String> userInfo) {
        if (permittedScopes.contains(ScopeEnum.OPENID.getName())) {
            if (Objects.isNull(userInfo)) {
                throw new UserinfoMissMatchedException();
            }
        }

        codeVerifierProvider.validate(consentVerifier);
        final ConsentParams consentParams = consentParamsService.get(consentVerifier)
                .orElseThrow(SessionExpiredException::new);
        consentParamsService.delete(consentVerifier);

        consentParams.setAud(aud);
        consentParams.setScopes(permittedScopes);
        consentParams.setUserinfo(userInfo);
        consentParamsService.create(consentVerifier, consentParams);

        return new ConsentAccept(consentVerifier);
    }

    @NonNull
    public LogoutInfo getLogoutInfo(@NonNull String sessionId, @NonNull String codeVerifier) {
        codeVerifierProvider.validate(codeVerifier);
        return logoutInfoService.get(sessionId).orElseThrow(SessionExpiredException::new);
    }

    @NonNull
    public LogoutAccept accept(@NonNull String codeVerifier, @NonNull String sessionId) {
        final LinkedMultiValueMap<String, String> attributes = new LinkedMultiValueMap<>();

        attributes.add(RedirectResponseEnum.LOGOUT_VERIFIER.getName(), codeVerifier);
        return new LogoutAccept(attributes);
    }
}

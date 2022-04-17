package ru.spbstu.university.authorizationserver.service.auth;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import ru.spbstu.university.authorizationserver.config.RedirectSettings;
import ru.spbstu.university.authorizationserver.model.AuthParams;
import ru.spbstu.university.authorizationserver.model.CompletedParams;
import ru.spbstu.university.authorizationserver.model.ConsentParams;
import ru.spbstu.university.authorizationserver.model.PkceParams;
import ru.spbstu.university.authorizationserver.model.User;
import ru.spbstu.university.authorizationserver.model.enums.GrantTypeEnum;
import ru.spbstu.university.authorizationserver.model.enums.ResponseTypeEnum;
import ru.spbstu.university.authorizationserver.service.AuthParamsService;
import ru.spbstu.university.authorizationserver.service.CompletedParamsService;
import ru.spbstu.university.authorizationserver.service.ConsentParamsService;
import ru.spbstu.university.authorizationserver.service.PkceParamsService;
import ru.spbstu.university.authorizationserver.service.auth.dto.redirect.RedirectResponse;
import ru.spbstu.university.authorizationserver.service.auth.dto.redirect.auth.RedirectAccessTokenResponse;
import ru.spbstu.university.authorizationserver.service.auth.dto.redirect.auth.RedirectCodeResponse;
import ru.spbstu.university.authorizationserver.service.auth.dto.redirect.consent.ConsentRedirect;
import ru.spbstu.university.authorizationserver.service.auth.dto.redirect.login.LoginRedirect;
import ru.spbstu.university.authorizationserver.service.auth.exception.SessionExpiredException;
import ru.spbstu.university.authorizationserver.service.auth.security.authcode.AuthCodeProvider;
import ru.spbstu.university.authorizationserver.service.auth.security.codeverifier.CodeVerifierProvider;
import ru.spbstu.university.authorizationserver.service.auth.security.token.access.AccessTokenProvider;

@Service
@AllArgsConstructor
public class AuthFlowManager {
    @NonNull
    private final RedirectSettings settings;
    @NonNull
    private final ConsentParamsService consentParamsService;
    @NonNull
    private final CodeVerifierProvider codeVerifierProvider;
    @NonNull
    private final AuthCodeProvider authCodeProvider;
    @NonNull
    private final AccessTokenProvider accessTokenProvider;
    @NonNull
    private final AuthParamsService authParamsService;
    @NonNull
    private final PkceParamsService pkceParamsService;
    @NonNull
    private final CompletedParamsService completedParamsService;

    @NonNull
    public RedirectResponse initFlow(@NonNull AuthParams authParams,
                                     @NonNull Optional<String> codeChallenge,
                                     @NonNull Optional<String> codeChallengeMethod) {
        if (authParams.getGrantTypes()
                .containsAll(List.of(GrantTypeEnum.AUTHORIZATION_CODE, GrantTypeEnum.REFRESH_TOKEN))
                && codeChallenge.isPresent() && codeChallengeMethod.isPresent()) {
            pkceParamsService.create(authParams.getState(), new PkceParams(codeChallenge.get(), codeChallengeMethod.get()));
        }

        final String verifier = codeVerifierProvider.generate();
        authParamsService.create(verifier, authParams);

        return new LoginRedirect(verifier, settings.getLogin());
    }

    @NonNull
    public RedirectResponse consentFlow(@NonNull User user, @NonNull String loginVerifier) {
        final AuthParams authParams = authParamsService.get(loginVerifier)
                .orElseThrow(SessionExpiredException::new);
        authParamsService.delete(loginVerifier);

        final String verifier = codeVerifierProvider.generate();
        consentParamsService.create(verifier,
                new ConsentParams(authParams, user));

        return new ConsentRedirect(verifier, settings.getConsent());
    }

    @NonNull
    public RedirectResponse consentFlow(@NonNull User user, @NonNull AuthParams authParams) {
        final String verifier = codeVerifierProvider.generate();
        consentParamsService.create(verifier, new ConsentParams(authParams, user));

        return new ConsentRedirect(verifier, settings.getConsent());
    }

    @NonNull
    public RedirectResponse endFlow(@NonNull String verifier) {
        final CompletedParams completedParams = completedParamsService.get(verifier)
                .orElseThrow(SessionExpiredException::new);
        completedParamsService.delete(verifier);
        final AuthParams authParams = completedParams.getConsentParams().getAuthParams();
        final List<GrantTypeEnum> grantTypes = authParams.getGrantTypes();
        final List<ResponseTypeEnum> responseTypes = authParams.getResponseTypes();

        if (grantTypes.contains(GrantTypeEnum.IMPLICIT) && responseTypes.contains(ResponseTypeEnum.TOKEN)) {
            final AccessTokenProvider.TokenInfo jwt = accessTokenProvider
                    .createJwt(completedParams.getConsentParams().getUser().getId(),
                            authParams.getClientInfo().getClientId(), authParams.getSessionId(),
                            completedParams.getAud(), Objects.requireNonNull(completedParams.getScopes()));

            return new RedirectAccessTokenResponse(authParams.getState(), jwt.getToken(), jwt.getExpiresIn(),
                    jwt.getScopes(), authParams.getRedirectUri());
        }

        final String code = authCodeProvider.generate();
        completedParamsService.create(code, completedParams);
        pkceParamsService.get(completedParams.getConsentParams().getAuthParams().getState())
                .ifPresent(pkceParams -> pkceParamsService.create(code, pkceParams));
        return new RedirectCodeResponse(code, authParams.getRedirectUri(),
                authParams.getState());
    }
}

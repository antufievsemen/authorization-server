package ru.spbstu.university.authorizationserver.service.flow;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import ru.spbstu.university.authorizationserver.config.SelfIssuerSettings;
import ru.spbstu.university.authorizationserver.config.LoginConsentLogoutProviderSettings;
import ru.spbstu.university.authorizationserver.model.PkceRequest;
import ru.spbstu.university.authorizationserver.model.RequestInfo;
import ru.spbstu.university.authorizationserver.model.RequestParams;
import ru.spbstu.university.authorizationserver.model.User;
import ru.spbstu.university.authorizationserver.model.enums.GrantTypesEnum;
import ru.spbstu.university.authorizationserver.model.enums.ResponseTypeEnum;
import ru.spbstu.university.authorizationserver.service.PkceRequestService;
import ru.spbstu.university.authorizationserver.service.RequestInfoService;
import ru.spbstu.university.authorizationserver.service.UserService;
import ru.spbstu.university.authorizationserver.service.encyption.access.AccessTokenEncryptorService;
import ru.spbstu.university.authorizationserver.service.encyption.authcode.AuthCodeEncryptorService;
import ru.spbstu.university.authorizationserver.service.flow.dto.RedirectResponse;
import ru.spbstu.university.authorizationserver.service.flow.dto.ValidRequest;
import ru.spbstu.university.authorizationserver.service.flow.dto.auth.RedirectAccessTokenResponse;
import ru.spbstu.university.authorizationserver.service.flow.dto.auth.RedirectCodeResponse;
import ru.spbstu.university.authorizationserver.service.flow.dto.consent.ConsentResponse;
import ru.spbstu.university.authorizationserver.service.flow.dto.enums.ResponseRedirect;
import ru.spbstu.university.authorizationserver.service.flow.dto.login.LoginResponse;
import ru.spbstu.university.authorizationserver.service.flow.exception.ConsentVerifierNotValidException;

@Service
@AllArgsConstructor
public class AuthManagerService {
    @NonNull
    private final PkceRequestService pkceRequestService;
    @NonNull
    private final LoginConsentLogoutProviderSettings settings;
    @NonNull
    private final RequestInfoService requestInfoService;
    @NonNull
    private final AuthCodeEncryptorService authCodeEncryptorService;
    @NonNull
    private final AccessTokenEncryptorService accessTokenEncryptorService;
    @NonNull
    private final UserService userService;

    @NonNull
    public RedirectResponse initAuthFlow(@NonNull String sessionId, @NonNull ValidRequest validRequest,
                                         @NonNull String nonce, @NonNull String state,
                                         @NonNull Optional<String> codeChallenge, @NonNull Optional<String> codeChallengeMethod,
                                         @NonNull Optional<String> consentVerifier) {
        final String challenge = UUID.randomUUID().toString();
        final RequestParams requestParams = new RequestParams(validRequest, nonce, state);

        final Optional<User> user = userService.getBySessionId(sessionId);
        if (validRequest.getGrantTypes().contains(GrantTypesEnum.PKCE) && codeChallenge.isPresent() &&
                codeChallengeMethod.isPresent()) {
            pkceRequestService.create(sessionId, new PkceRequest(codeChallenge.get(), codeChallengeMethod.get()));
        }

        if (user.isEmpty()) {
            requestInfoService.create(challenge, new RequestInfo(null, requestParams, ResponseRedirect.LOGIN_CHALLENGE));
            return new LoginResponse(challenge, settings.getLogin());
        }
        if (consentVerifier.isEmpty()) {
            requestInfoService.create(challenge, new RequestInfo(user.get().getSub(), requestParams, ResponseRedirect.CONSENT_CHALLENGE));
            return new ConsentResponse(challenge, settings.getConsent());
        }

        final RequestInfo requestInfo = requestInfoService.get(consentVerifier.get()).orElseThrow(ConsentVerifierNotValidException::new);
        if (requestInfo.getResponseRedirect() != ResponseRedirect.CONSENT_VERIFIER) {
            throw new ConsentVerifierNotValidException();
        }

        if (validRequest.getResponseTypeEnum() == ResponseTypeEnum.TOKEN) {
            final AccessTokenEncryptorService.TokenInfo jwt = accessTokenEncryptorService.createJwt(Objects.requireNonNull(requestInfo.getSubject()),
                    requestInfo.getRequestParams().getClientId(), requestInfo.getRequestParams().getNonce(),
                    requestInfo.getRequestParams().getMap().get("granted_access_token_audience"),
                    requestInfo.getRequestParams().getMap().get("granted_scopes"));

            return new RedirectAccessTokenResponse(state, jwt.getToken(), jwt.getExpiresIn(),
                    requestInfo.getRequestParams().getMap().get("granted_scopes"),
                    requestInfo.getRequestParams().getRedirectUri());
        }

        final String code = authCodeEncryptorService.generate();
        requestInfoService.delete(consentVerifier.get());
        requestInfoService.create(code, requestInfo);
        return new RedirectCodeResponse(code, requestInfo.getRequestParams().getRedirectUri(),
                requestInfo.getRequestParams().getState());
    }
}

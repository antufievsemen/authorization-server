package ru.spbstu.university.authorizationserver.service.flow;

import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import ru.spbstu.university.authorizationserver.config.ConsentRedirectSettings;
import ru.spbstu.university.authorizationserver.config.LoginRedirectSettings;
import ru.spbstu.university.authorizationserver.model.PkceRequest;
import ru.spbstu.university.authorizationserver.model.RequestParams;
import ru.spbstu.university.authorizationserver.model.User;
import ru.spbstu.university.authorizationserver.model.enums.GrantTypesEnum;
import ru.spbstu.university.authorizationserver.model.enums.ResponseTypeEnum;
import ru.spbstu.university.authorizationserver.service.PkceRequestService;
import ru.spbstu.university.authorizationserver.service.RequestParamsService;
import ru.spbstu.university.authorizationserver.service.UserService;
import ru.spbstu.university.authorizationserver.service.encyption.access.AccessTokenEncryptorService;
import ru.spbstu.university.authorizationserver.service.encyption.authcode.AuthCodeEncryptorService;
import ru.spbstu.university.authorizationserver.service.flow.dto.AuthResponse;
import ru.spbstu.university.authorizationserver.service.flow.dto.auth.AuthAccessTokenResponse;
import ru.spbstu.university.authorizationserver.service.flow.dto.auth.AuthCodeResponse;
import ru.spbstu.university.authorizationserver.service.flow.dto.consent.ConsentResponse;
import ru.spbstu.university.authorizationserver.service.flow.dto.login.LoginResponse;
import ru.spbstu.university.authorizationserver.service.generator.Generator;

@Service
@AllArgsConstructor
public class FlowManagerService {
    @NonNull
    private final PkceRequestService pkceRequestService;
    @NonNull
    private final LoginRedirectSettings loginRedirectSettings;
    @NonNull
    private final ConsentRedirectSettings consentRedirectSettings;
    @NonNull
    private final RequestParamsService requestParamsService;
    @NonNull
    private final Generator<String> idGenerator;
    @NonNull
    private final AuthCodeEncryptorService authCodeEncryptorService;
    @NonNull
    private final AccessTokenEncryptorService accessTokenEncryptorService;
    @NonNull
    private final UserService userService;

    @NonNull
    public AuthResponse initFlow(@NonNull String sessionId, @NonNull AuthService.ValidateRequest validateRequest,
                                 @NonNull String nonce, @NonNull String state,
                                 @NonNull Optional<String> codeChallenge, @NonNull Optional<String> codeChallengeMethod) {
        final String challenge = idGenerator.generate();
        final Optional<User> user = userService.getBySessionId(sessionId);
        if (validateRequest.getGrantTypes().contains(GrantTypesEnum.PKCE) && codeChallenge.isPresent() &&
                codeChallengeMethod.isPresent()) {
            pkceRequestService.create(state, new PkceRequest(codeChallenge.get(), codeChallengeMethod.get()));
        }
        final RequestParams requestParams = new RequestParams(challenge, getParamMap(validateRequest, nonce, state));
        requestParamsService.create(requestParams);

        if (user.isEmpty()) {
            return new LoginResponse(challenge, loginRedirectSettings.getUrl());
        }

        if (!user.get().isConsented()) {
            new ConsentResponse(challenge, consentRedirectSettings.getUrl());
        }

        if (validateRequest.getResponseTypeEnum() == ResponseTypeEnum.TOKEN) {
            final AccessTokenEncryptorService.TokenInfo jwt = accessTokenEncryptorService.createJwt(user.get().getSub(),
                    validateRequest.getClientId(), nonce, null, validateRequest.getScopes());
            return new AuthAccessTokenResponse(state, jwt.getToken(), jwt.getExpiresIn(), validateRequest.getScopes(),
                    validateRequest.getRedirectUri());
        }

        return new AuthCodeResponse(authCodeEncryptorService.generate(), validateRequest.getRedirectUri(), state);
    }


    @NonNull
    private MultiValueMap<String, String> getParamMap(@NonNull AuthService.ValidateRequest validateRequest, @NonNull String nonce,
                                                      @NonNull String state) {
        final MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", validateRequest.getClientId());
        map.add("response_type", validateRequest.getResponseTypeEnum().getName());
        map.put("scopes", validateRequest.getScopes());
        map.add("redirect_uri", validateRequest.getRedirectUri());
        map.add("nonce", nonce);
        map.add("state", state);

        return map;
    }
}

package ru.spbstu.university.authorizationserver.model;

import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import ru.spbstu.university.authorizationserver.service.flow.dto.ValidRequest;

@Getter
public class RequestParams {
    @NonNull
    private final MultiValueMap<String, String> map;

    @NonNull
    public RequestParams(@NonNull ValidRequest validRequest, @NonNull String nonce,
                         @NonNull String state) {
        final MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", validRequest.getClientId());
        map.add("response_type", validRequest.getResponseTypeEnum().getName());
        map.put("scopes", validRequest.getScopes());
        map.add("redirect_uri", validRequest.getRedirectUri());
        map.add("nonce", nonce);
        map.add("state", state);

        this.map = map;
    }

    @NonNull
    public String getResponseType() {
        return Objects.requireNonNull(map.getFirst("response_type"));
    }

    @NonNull
    public String getClientId() {
        return Objects.requireNonNull(map.getFirst("client_id"));
    }

    @NonNull
    public List<String> getScopes() {
        return map.get("scopes");
    }

    @NonNull
    public String getNonce() {
        return Objects.requireNonNull(map.getFirst("nonce"));
    }

    @NonNull
    public String getState() {
        return Objects.requireNonNull(map.getFirst("state"));
    }

    @NonNull
    public String getRedirectUri() {
        return Objects.requireNonNull(map.getFirst("redirect_uri"));
    }
}

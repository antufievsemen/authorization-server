package ru.spbstu.university.authorizationserver.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
public class ClientResponse {
    @JsonProperty(value = "client_id")
    @NonNull
    private final String clientId;
    @JsonProperty(value = "client_secret")
    @NonNull
    private final String clientSecret;
    @JsonProperty(value = "grant_types")
    @NonNull
    private final List<String> grantTypes;
    @NonNull
    private final List<String> scopes;
    @JsonProperty(value = "response_types")
    @NonNull
    private final List<String> responseTypes;
    @JsonProperty(value = "redirect_uri")
    @NonNull
    private final List<String> callbacks;
}

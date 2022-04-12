package ru.spbstu.university.authorizationserver.model;

import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.lang.Nullable;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CompletedParams {
    @NonNull
    private ConsentParams consentParams;
    @Nullable
    private List<String> aud;
    @Nullable
    private List<String> scopes;
    @Nullable
    private Map<String, String> userInfo;
}

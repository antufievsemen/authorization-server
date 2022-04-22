package ru.spbstu.university.authorizationserver.model.cache;

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
    @NonNull
    private List<String> aud;
    @NonNull
    private List<String> scopes;
    @Nullable
    private Map<String, String> openidInfo;
}

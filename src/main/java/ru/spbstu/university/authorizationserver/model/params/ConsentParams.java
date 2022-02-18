package ru.spbstu.university.authorizationserver.model.params;

import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

@Setter
@Getter
@NoArgsConstructor
@RequiredArgsConstructor
public class ConsentParams {
    @Nullable
    private List<String> scopes;
    @Nullable
    private List<String> aud;
    @Nullable
    private Map<String, String> userinfo;
    @NonNull
    private AuthParams authParams;
}

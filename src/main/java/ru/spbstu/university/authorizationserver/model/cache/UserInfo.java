package ru.spbstu.university.authorizationserver.model.cache;

import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import ru.spbstu.university.authorizationserver.model.Scope;
import ru.spbstu.university.authorizationserver.model.User;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {
    @NonNull
    private String id;
    @NonNull
    private List<String> scopes;
    @NonNull
    private String sessionId;
    @NonNull
    private String state;

    public UserInfo(@NonNull User user) {
        this.id = user.getId();
        this.scopes = user.getScopes().stream().map(Scope::getName).collect(Collectors.toList());
        this.sessionId = user.getSessionId();
        this.state = user.getState();
    }
}

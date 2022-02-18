package ru.spbstu.university.authorizationserver.util;

import java.util.List;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import ru.spbstu.university.authorizationserver.model.Callback;
import ru.spbstu.university.authorizationserver.model.GrantType;
import ru.spbstu.university.authorizationserver.model.ResponseType;
import ru.spbstu.university.authorizationserver.model.Scope;

@UtilityClass
public class CollectionConverter {

    @NonNull
    public static List<String> scopesGetNames(@NonNull List<Scope> scopes) {
        return scopes.stream()
                .map(Scope::getName)
                .collect(Collectors.toList());
    }

    @NonNull
    public static List<String> responseTypesGetNames(@NonNull List<ResponseType> scopes) {
        return scopes.stream()
                .map(responseType -> responseType.getType().getName())
                .collect(Collectors.toList());
    }

    @NonNull
    public static List<String> grantTypesGetNames(@NonNull List<GrantType> grantTypes) {
        return grantTypes.stream()
                .map(grantType -> grantType.getType().getName())
                .collect(Collectors.toList());
    }

    @NonNull
    public static List<String> callbacksGetUrls(@NonNull List<Callback> callbacks) {
        return callbacks.stream()
                .map(Callback::getUrl)
                .collect(Collectors.toList());
    }
}

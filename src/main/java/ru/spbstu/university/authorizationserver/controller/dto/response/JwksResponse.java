package ru.spbstu.university.authorizationserver.controller.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
public class JwksResponse {
    @NonNull
    private final List<Key> keys;

    @Getter
    @AllArgsConstructor
    public static class Key {
        @NonNull
        private final String alg;
        @NonNull
        private final String e;
        private final byte[] n;
        @NonNull
        private final String kid;
        @NonNull
        private final String kty;
        @NonNull
        private final String use;
    }
}



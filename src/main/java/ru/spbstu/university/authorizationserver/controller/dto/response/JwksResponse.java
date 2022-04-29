package ru.spbstu.university.authorizationserver.controller.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;

@Getter
@AllArgsConstructor
public class JwksResponse {
    @NonNull
    private final List<Key> keys;

    @Getter
    public static class Key {
        @NonNull
        private final String kid;
        @NonNull
        private final String alg;
        @NonNull
        private final String use;
        @NonNull
        private final String kty;
        @NonNull
        private final String x5c;
        @NonNull
        private final String e;
        private final String n;

        @SneakyThrows
        public Key(@NonNull String alg, @NonNull String e, @NonNull String n, @NonNull String kid,
                   @NonNull String kty, @NonNull String use, @NonNull String x5c) {
            this.alg = alg;
            this.e = e;
            this.n = n;
            this.kid = kid;
            this.kty = kty;
            this.use = use;
            this.x5c = x5c;
        }
    }
}



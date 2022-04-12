package ru.spbstu.university.authorizationserver.service.auth.security;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.spbstu.university.authorizationserver.model.KeySet;
import ru.spbstu.university.authorizationserver.model.enums.TokenEnum;
import ru.spbstu.university.authorizationserver.repository.KeySetRepository;
import ru.spbstu.university.authorizationserver.service.generator.impl.IdGenerator;

@Service
@Transactional
@AllArgsConstructor
public class JwksService {
    @NonNull
    private final KeySetRepository keySetRepository;
    @NonNull
    private final IdGenerator idGenerator;

    @NonNull
    public KeySet getOrCreateKeyPair(@NonNull String clientId, @NonNull TokenEnum tokenType) {
        final Optional<KeySet> keySet = getByClientId(clientId, tokenType);
        if (keySet.isPresent()) {
            return keySet.get();
        }

        final KeyPair keyPair = generateKeyPair("RSA");
        return keySetRepository.save(new KeySet(idGenerator.generate(), clientId, tokenType, "RSA",
                keyPair.getPrivate().toString(), keyPair.getPublic().toString()));
    }

    @NonNull
    public List<KeyDto> getAll() {
        return keySetRepository.findAll().stream()
                .map(ks -> new KeyDto(ks.getId(), ks.getAlg(), ks.getPublicKey()))
                .collect(Collectors.toList());
    }

    @NonNull
    public Optional<KeySet> getByClientId(@NonNull String clientId, @NonNull TokenEnum tokenType) {
        return keySetRepository.findByClientIdAndTokenType(clientId, tokenType);
    }

    @NonNull
    @SneakyThrows
    private KeyPair generateKeyPair(@NonNull String alg) {
        final KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(alg);
        return keyPairGenerator.generateKeyPair();
    }


    @Getter
    @AllArgsConstructor
    public static class KeyDto {
        @NonNull
        private final String id;
        @NonNull
        private final String alg;
        @NonNull
        private final String publicKey;
    }
}

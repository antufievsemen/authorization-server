package ru.spbstu.university.authorizationserver.service.auth.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.FileInputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.spbstu.university.authorizationserver.model.Jwk;
import ru.spbstu.university.authorizationserver.repository.JwksRepository;
import ru.spbstu.university.authorizationserver.service.generator.impl.IdGenerator;

@Service
@Transactional
public class JwksService {
    @NonNull
    private final JwksRepository jwksRepository;
    @NonNull
    private final IdGenerator idGenerator;
    @NonNull
    private final String x5c;

    @SneakyThrows
    public JwksService(@NonNull JwksRepository jwksRepository, @NonNull IdGenerator idGenerator) {
        this.jwksRepository = jwksRepository;
        this.idGenerator = idGenerator;
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X509");
        final X509Certificate certificate =
                (X509Certificate) certificateFactory
                        .generateCertificate(new FileInputStream("src/main/resources/x509-certificate.crt"));
        this.x5c = new ObjectMapper().writeValueAsString(certificate.getEncoded());
    }

    @NonNull
    public Jwk getOrCreateJwk(@NonNull String clientId) {
        final Optional<Jwk> jwk = getByClientId(clientId);
        if (jwk.isPresent()) {
            return jwk.get();
        }

        final Keys keys = generateKeys("RSA");
        return jwksRepository.save(new Jwk(idGenerator.generate(), clientId, "RSA256", "sign", "RSA",
                keys.getRsaPrivateKey().getEncoded(), keys.getRsaPublicKey().getModulus().toByteArray(),
                keys.getRsaPublicKey().getPublicExponent().toByteArray()));
    }

    @NonNull
    public List<JwkDto> getAll() {
        return jwksRepository.findAll().stream()
                .map(ks -> new JwkDto(ks.getKid(), ks.getAlg(), ks.getUse(), ks.getKty(), getBase64Encode(ks.getN()),
                        getBase64Encode(ks.getE()), x5c))
                .collect(Collectors.toList());
    }

    @NonNull
    public Optional<Jwk> get(@NonNull String id) {
        return jwksRepository.findByKid(id);
    }

    @NonNull
    public Optional<Jwk> getByClientId(@NonNull String clientId) {
        return jwksRepository.findByClientId(clientId);
    }

    @NonNull
    @SneakyThrows
    private Keys generateKeys(@NonNull String alg) {
        final KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(alg);
        final KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyPair.getPrivate().getEncoded());
        RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
        return new Keys(pkcs8EncodedKeySpec, rsaPublicKey);
    }

    @NonNull
    private String getBase64Encode(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }


    @Getter
    @AllArgsConstructor
    public static class JwkDto {
        @NonNull
        private String kid;
        @NonNull
        private String alg;
        @NonNull
        private String use;
        @NonNull
        private String kty;
        @NonNull
        private String n;
        @NonNull
        private String e;
        @NonNull
        private String x5c;
    }

    @Getter
    @AllArgsConstructor
    private static class Keys {
        @NonNull
        private final PKCS8EncodedKeySpec rsaPrivateKey;
        @NonNull
        private final RSAPublicKey rsaPublicKey;
    }
}

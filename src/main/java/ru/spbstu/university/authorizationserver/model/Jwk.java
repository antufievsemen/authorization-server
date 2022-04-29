package ru.spbstu.university.authorizationserver.model;

import java.math.BigInteger;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import ru.spbstu.university.authorizationserver.model.enums.TokenEnum;

@Entity(name = "key_sets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Jwk {
    @Id
    @NonNull
    private String kid;
    @NonNull
    private String clientId;
    @NonNull
    private String alg;
    @NonNull
    private String use;
    @NonNull
    private String kty;
    private byte[] privateKey;
    private byte[] n;
    private byte[] e;
}

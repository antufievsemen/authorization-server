package ru.spbstu.university.authorizationserver.model;

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

@Entity(name = "key_set")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KeySet {
    @Id
    @NonNull
    private String id;
    @NonNull
    private String clientId;
    @NonNull
    @Enumerated(EnumType.STRING)
    private TokenEnum tokenType;
    @NonNull
    private String alg;
    @NonNull
    private String privateKey;
    @NonNull
    private String publicKey;
}

package ru.spbstu.university.authorizationserver.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
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
    private byte[] privateKey;
    private byte[] publicKey;
}

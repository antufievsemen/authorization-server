package ru.spbstu.university.authorizationserver.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.lang.Nullable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginInfo {
    @Id
    @NonNull
    private String sub;
    @OneToOne(fetch = FetchType.EAGER)
    @NonNull
    private Client client;
    @Nullable
    private String availableScope;
    @Nullable
    private String sessionId;
}

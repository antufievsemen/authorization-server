package ru.spbstu.university.authorizationserver.model;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
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
    @CreationTimestamp
    private LocalDateTime createdAt;
}

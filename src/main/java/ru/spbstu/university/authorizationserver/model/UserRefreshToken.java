package ru.spbstu.university.authorizationserver.model;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.lang.Nullable;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class UserRefreshToken {
    @Id
    @NonNull
    private String id;
    @NonNull
    @OneToOne(fetch = FetchType.EAGER)
    private User user;
    @Nullable
    private String refreshToken;
    @UpdateTimestamp
    private LocalDateTime createAt;
    @NonNull
    private LocalDateTime expiredAt;

    public UserRefreshToken(@NonNull String id, @NonNull User user, @Nullable String refreshToken, @NonNull LocalDateTime expiredAt) {
        this.id = id;
        this.user = user;
        this.refreshToken = refreshToken;
        this.expiredAt = expiredAt;
    }
}

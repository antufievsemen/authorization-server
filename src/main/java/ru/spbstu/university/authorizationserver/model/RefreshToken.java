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
import org.hibernate.annotations.UpdateTimestamp;

@Entity(name = "refresh_tokens")
@Getter
@Setter
@NoArgsConstructor
public class RefreshToken {
    @Id
    @NonNull
    private String id;
    @NonNull
    @OneToOne(fetch = FetchType.EAGER)
    private User user;
    @NonNull
    private String token;
    @UpdateTimestamp
    private LocalDateTime createdAt;
    @NonNull
    private LocalDateTime expiredAt;

    public RefreshToken(@NonNull String id, @NonNull User user, @NonNull String token,
                        @NonNull LocalDateTime expiredAt) {
        this.id = id;
        this.user = user;
        this.token = token;
        this.expiredAt = expiredAt;
    }
}

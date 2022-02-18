package ru.spbstu.university.authorizationserver.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.Instant;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Callback {
    @Id
    @NonNull
    private String id;
    @NonNull
    private String url;
    @JsonIgnore
    @CreationTimestamp
    private Instant createdAt;

    public Callback(@NonNull String id, @NonNull String url) {
        this.id = id;
        this.url = url;
    }
}

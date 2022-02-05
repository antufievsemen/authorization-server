package ru.spbstu.university.authorizationserver.model;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
public class User {
    @Id
    @Column(name = "id")
    @NonNull
    private String sub;
    @OneToOne(fetch = FetchType.EAGER)
    @NonNull
    private Client client;
    @NonNull
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "user_scopes",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "scope_id"))
    private List<Scope> scopes;
    @NonNull
    private String sessionId;
    @CreationTimestamp
    private LocalDateTime createdAt;

    public User(@NonNull String sub, @NonNull Client client, @NonNull List<Scope> scopes, @NonNull String sessionId) {
        this.sub = sub;
        this.client = client;
        this.scopes = scopes;
        this.sessionId = sessionId;
    }
}

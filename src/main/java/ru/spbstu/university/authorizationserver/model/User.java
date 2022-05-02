package ru.spbstu.university.authorizationserver.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

@Entity(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    @NonNull
    private String id;
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
    @NonNull
    private String state;
    @CreationTimestamp
    private Date createdAt;

    public User(@NonNull String id, @NonNull Client client, @NonNull List<Scope> scopes, @NonNull String sessionId,
                @NonNull String state) {
        this.id = id;
        this.client = client;
        this.scopes = scopes;
        this.sessionId = sessionId;
        this.state = state;
    }
}

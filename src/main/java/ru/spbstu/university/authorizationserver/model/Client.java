package ru.spbstu.university.authorizationserver.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.Instant;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.UpdateTimestamp;


@Entity(name = "client")
@Getter
@Setter
@NoArgsConstructor
public class Client {
    @Id
    @NonNull
    private String id;
    @NonNull
    private String clientId;
    @NonNull
    @JsonIgnoreProperties
    private String clientSecret;
    @NonNull
    @Fetch(FetchMode.SUBSELECT)
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "client_grant_types",
            joinColumns = @JoinColumn(name = "client_id"),
            inverseJoinColumns = @JoinColumn(name = "grant_type_id"))
    private List<GrantType> grantTypes;
    @NonNull
    @Fetch(FetchMode.SUBSELECT)
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "client_scopes",
            joinColumns = @JoinColumn(name = "client_id"),
            inverseJoinColumns = @JoinColumn(name = "scope_id"))
    private List<Scope> scopes;
    @NonNull
    @Fetch(FetchMode.SUBSELECT)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "client_response_types",
            joinColumns = @JoinColumn(name = "client_id"),
            inverseJoinColumns = @JoinColumn(name = "response_type_id"))
    private List<ResponseType> responseTypes;
    @NonNull
    @Fetch(FetchMode.SUBSELECT)
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinTable(name = "client_callbacks",
            joinColumns = @JoinColumn(name = "client_id"),
            inverseJoinColumns = @JoinColumn(name = "callback_id"))
    private List<Callback> callbacks;

    public Client(@NonNull String id, @NonNull String clientId, @NonNull String clientSecret,
                  @NonNull List<GrantType> grantTypes, @NonNull List<Scope> scopes,
                  @NonNull List<ResponseType> responseTypes, @NonNull List<Callback> callbacks) {
        this.id = id;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.grantTypes = grantTypes;
        this.scopes = scopes;
        this.responseTypes = responseTypes;
        this.callbacks = callbacks;
    }

    @CreationTimestamp
    @Column(updatable = false)
    private Instant createdAt;
    @UpdateTimestamp
    private Instant updatedAt;
}

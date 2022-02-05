package ru.spbstu.university.authorizationserver.model;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.lang.Nullable;
import ru.spbstu.university.authorizationserver.model.enums.ResponseTypeEnum;


@Entity(name = "client")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Client {
    @Id
    @NonNull
    private String id;
    @NonNull
    private String clientId;
    @NonNull
    private String clientSecret;
    @Fetch(FetchMode.SUBSELECT)
    @NonNull
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "client_grant_type",
            joinColumns = @JoinColumn(name = "client_id"),
            inverseJoinColumns = @JoinColumn(name = "grant_type_id"))
    private List<GrantType> grantTypes;
    @NonNull
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "client_scopes",
            joinColumns = @JoinColumn(name = "client_id"),
            inverseJoinColumns = @JoinColumn(name = "scope_id"))
    private List<Scope> scopes;
    @NonNull
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "client_scopes",
            joinColumns = @JoinColumn(name = "client_id"),
            inverseJoinColumns = @JoinColumn(name = "scope_id"))
    private List<ResponseType> responseTypes;
    @Nullable
    private String callback;//rename callback
}

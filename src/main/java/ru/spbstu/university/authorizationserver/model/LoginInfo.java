package ru.spbstu.university.authorizationserver.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
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
    @NonNull
    private String clientId;
    @Nullable
    private String availableScope;
}

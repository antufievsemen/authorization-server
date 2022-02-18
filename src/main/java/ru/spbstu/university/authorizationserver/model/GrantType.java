package ru.spbstu.university.authorizationserver.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import ru.spbstu.university.authorizationserver.model.enums.GrantTypeEnum;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GrantType {
    @Id
    @NonNull
    private String id;
    @NonNull
    @Enumerated(EnumType.STRING)
    private GrantTypeEnum type;
}

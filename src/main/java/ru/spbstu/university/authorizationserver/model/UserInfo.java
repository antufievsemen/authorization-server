package ru.spbstu.university.authorizationserver.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import ru.spbstu.university.authorizationserver.model.enums.Gender;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {
    @Id
    @NonNull
    private String id;
    @NonNull
    private String firstname;
    @NonNull
    private String lastname;
    @NonNull
    private Gender gender;
    @NonNull
    private String phoneNumber;
    private boolean phoneVerified;
    @NonNull
    private String email;
    private boolean emailVerified;
}

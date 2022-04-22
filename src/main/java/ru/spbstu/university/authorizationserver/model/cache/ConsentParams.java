package ru.spbstu.university.authorizationserver.model.cache;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ConsentParams {
    @NonNull
    private AuthParams authParams;
    @NonNull
    private UserInfo userInfo;
}

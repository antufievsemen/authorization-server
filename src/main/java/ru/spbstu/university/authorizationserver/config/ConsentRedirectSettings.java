package ru.spbstu.university.authorizationserver.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ConsentRedirectSettings {
    @NonNull
    private String url;
}

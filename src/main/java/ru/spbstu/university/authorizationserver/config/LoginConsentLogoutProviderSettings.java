package ru.spbstu.university.authorizationserver.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@NoArgsConstructor
@ConfigurationProperties(prefix = "application.urls")
public class LoginConsentLogoutProviderSettings {
    @NonNull
    private String login;
    @NonNull
    private String logout;
    @NonNull
    private String consent;
}
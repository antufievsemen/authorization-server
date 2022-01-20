package ru.spbstu.university.authorizationserver.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//todo Сделать открытымы эндпоинты и закрыть флоу
        http
                .authorizeRequests().antMatchers("/**").permitAll();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().anyRequest();
    }
}

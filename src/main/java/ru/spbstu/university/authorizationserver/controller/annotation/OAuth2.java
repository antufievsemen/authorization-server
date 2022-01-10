package ru.spbstu.university.authorizationserver.controller.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/oauth2")
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface OAuth2 {
}

package ru.spbstu.university.authorizationserver.config;

import lombok.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import ru.spbstu.university.authorizationserver.model.LogoutInfo;
import ru.spbstu.university.authorizationserver.model.PkceRequest;
import ru.spbstu.university.authorizationserver.model.RequestInfo;

@Configuration
public class RedisLettuceConfig {

    @Bean
    //token - token
    public RedisTemplate<String, String> revokeTokenTemplate() {
        final RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(new LettuceConnectionFactory());
        return template;
    }

    @Bean
    //id(challenge, auth code, access token) - request params
    public RedisTemplate<String, RequestInfo> requestInfoRedisTemplate() {
        final RedisTemplate<String, RequestInfo> template = new RedisTemplate<>();
        template.setConnectionFactory(new LettuceConnectionFactory());
        return template;
    }

    @Bean
    //sessionId - pkce request
    public RedisTemplate<String, PkceRequest> pkceRequestRedisTemplate() {
        final RedisTemplate<String, PkceRequest> template = new RedisTemplate<>();
        template.setConnectionFactory(new LettuceConnectionFactory());
        return template;
    }

    @Bean
    public RedisTemplate<String, LogoutInfo> logoutInfoRedisTemplate() {
        final RedisTemplate<String, LogoutInfo> template = new RedisTemplate<>();
        template.setConnectionFactory(new LettuceConnectionFactory());
        return template;
    }

}

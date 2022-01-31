package ru.spbstu.university.authorizationserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import ru.spbstu.university.authorizationserver.model.PkceRequest;
import ru.spbstu.university.authorizationserver.model.RequestParams;

@Configuration
public class RedisLettuceConfig {

    @Bean
    public RedisTemplate<String, String> accessTokenTemplate() {
        final RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(new LettuceConnectionFactory());
        return template;
    }

    @Bean
    public RedisTemplate<String, RequestParams> requestParamsRedisTemplate() {
        final RedisTemplate<String, RequestParams> template = new RedisTemplate<>();
        template.setConnectionFactory(new LettuceConnectionFactory());
        return template;
    }

    @Bean
    public RedisTemplate<String, PkceRequest> pkceRequestRedisTemplate() {
        final RedisTemplate<String, PkceRequest> template = new RedisTemplate<>();
        template.setConnectionFactory(new LettuceConnectionFactory());
        return template;
    }

    @Bean
    public RedisTemplate<String, String> authCodeTemplate() {
        final RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        return redisTemplate;
    }

}

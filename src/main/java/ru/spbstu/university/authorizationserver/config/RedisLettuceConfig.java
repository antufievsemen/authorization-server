package ru.spbstu.university.authorizationserver.config;

import java.time.LocalDateTime;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisLettuceConfig {

    @Bean
    public LettuceConnectionFactory lettuceConnectionFactory() {
        return new LettuceConnectionFactory();
    }

    @Bean
    public RedisTemplate<String, LocalDateTime> redisTemplate() {
        RedisTemplate<String, LocalDateTime> template = new RedisTemplate<>();
        template.setConnectionFactory(lettuceConnectionFactory());

        return template;
    }
}

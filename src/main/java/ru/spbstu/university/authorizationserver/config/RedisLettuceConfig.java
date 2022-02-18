package ru.spbstu.university.authorizationserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import ru.spbstu.university.authorizationserver.model.params.AuthParams;
import ru.spbstu.university.authorizationserver.model.params.ConsentParams;
import ru.spbstu.university.authorizationserver.model.params.PkceRequestParams;
import ru.spbstu.university.authorizationserver.service.auth.dto.logout.LogoutInfo;

@Configuration
public class RedisLettuceConfig {

//    @Bean
//    //token - token
//    public RedisTemplate<String, String> revokeTokenTemplate() {
//        final RedisTemplate<String, String> template = new RedisTemplate<>();
//        template.setConnectionFactory(new LettuceConnectionFactory());
//        return template;
//    }

    @Bean
    public LettuceConnectionFactory connectionFactory() {
        return new LettuceConnectionFactory("localhost", 16379);
    }

    @Bean
    //sessionId - request params
    public RedisTemplate<String, AuthParams> authRequestParamsRedisTemplate(RedisConnectionFactory connectionFactory) {
        final RedisTemplate<String, AuthParams> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setEnableTransactionSupport(true);
        return template;
    }

    @Bean
    //sessionId - pkce request
    public RedisTemplate<String, PkceRequestParams> pkceRequestRedisTemplate(RedisConnectionFactory connectionFactory) {
        final RedisTemplate<String, PkceRequestParams> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setEnableTransactionSupport(true);
        return template;
    }

    @Bean
    public RedisTemplate<String, LogoutInfo> logoutInfoRedisTemplate(RedisConnectionFactory connectionFactory) {
        final RedisTemplate<String, LogoutInfo> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setEnableTransactionSupport(true);
        return template;
    }

//    @Bean
//    //sessionId - auth code
//    public RedisTemplate<String, String> authCodeRedisTemplate() {
//        final RedisTemplate<String, String> template = new RedisTemplate<>();
//        template.setConnectionFactory(new LettuceConnectionFactory());
//        return template;
//    }

    @Bean
    //session- consent request params
    public RedisTemplate<String, ConsentParams> consentRequestParamsRedisTemplate(
            RedisConnectionFactory connectionFactory) {
        final RedisTemplate<String, ConsentParams> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setEnableTransactionSupport(true);
        return template;
    }
}

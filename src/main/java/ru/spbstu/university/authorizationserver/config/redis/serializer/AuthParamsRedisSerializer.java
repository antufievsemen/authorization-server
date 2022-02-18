package ru.spbstu.university.authorizationserver.config.redis.serializer;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.lang.Nullable;
import ru.spbstu.university.authorizationserver.model.params.AuthParams;

public class AuthParamsRedisSerializer implements RedisSerializer<AuthParams> {
    @Nullable
    @Override
    public byte[] serialize(@Nullable AuthParams authParams) throws SerializationException {
        return new byte[0];
    }

    @Nullable
    @Override
    public AuthParams deserialize(@Nullable byte[] bytes) throws SerializationException {
        return null;
    }
}

package ru.spbstu.university.authorizationserver.service.generator.impl;

import com.devskiller.friendly_id.FriendlyId;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import ru.spbstu.university.authorizationserver.service.generator.Generator;

@Service
public class IdGenerator implements Generator<String> {

    @NonNull
    @Override
    public String generate() {
        return FriendlyId.createFriendlyId();
    }
}

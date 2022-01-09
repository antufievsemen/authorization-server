package ru.spbstu.university.authorizationserver.service.idgenerator;

import com.devskiller.friendly_id.FriendlyId;
import lombok.NonNull;
import org.springframework.stereotype.Service;

@Service
public class IdGenerator implements Generator {

    @NonNull
    @Override
    public String generate() {
        return FriendlyId.createFriendlyId();
    }
}

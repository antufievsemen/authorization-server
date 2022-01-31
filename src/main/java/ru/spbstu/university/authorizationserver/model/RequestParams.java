package ru.spbstu.university.authorizationserver.model;

import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.util.MultiValueMap;

@Getter
@AllArgsConstructor
public class RequestParams {
    @NonNull
    private final String challenge;
    @NonNull
    private final MultiValueMap<String, String> map;
}

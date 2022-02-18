package ru.spbstu.university.authorizationserver.model.params;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import ru.spbstu.university.authorizationserver.model.enums.GrantTypeEnum;
import ru.spbstu.university.authorizationserver.model.enums.ResponseTypeEnum;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AuthClient {
    @NonNull
    private String clientId;
    @NonNull
    private List<GrantTypeEnum> grantTypeEnums;
    @NonNull
    private List<ResponseTypeEnum> responseTypes;
    @NonNull
    private List<String> scopes;
    @NonNull
    private String callbacks;
}

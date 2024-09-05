package hdxian.login.web.login;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data // @Getter, @Setter, @RequiredArgsConstructor, @EqualsAndHashCode, @ToString
public class LoginForm { // for binding login request parameter

    @NotEmpty
    private String loginId;

    @NotEmpty
    private String password;

}

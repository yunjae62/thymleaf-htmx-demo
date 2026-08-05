package ex.htmxdemo.user.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@Getter
@RequiredArgsConstructor
public enum UserRole implements GrantedAuthority {

    USER(Authorities.USER),
    ADMIN(Authorities.ADMIN);

    private final String authority;

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Authorities {

        public static final String USER = "ROLE_USER";
        public static final String ADMIN = "ROLE_ADMIN";
    }
}

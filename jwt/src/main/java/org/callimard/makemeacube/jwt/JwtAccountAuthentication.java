package org.callimard.makemeacube.jwt;

import com.google.common.base.Objects;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Getter
public class JwtAccountAuthentication extends AbstractAuthenticationToken {

    // Variables.

    @NonNull
    private final JwtAccount jwtAccount;

    @NonNull
    private final String jwt;

    // Constructors.

    public JwtAccountAuthentication(@NonNull JwtAccount principal, @NonNull String credentials) {
        super(principal.getPrivileges().stream().map(SimpleGrantedAuthority::new).toList());
        this.setAuthenticated(true);
        this.jwtAccount = principal;
        this.jwt = credentials;
    }

    // Methods.

    @Override
    public String getCredentials() {
        return jwt;
    }

    @Override
    public JwtAccount getPrincipal() {
        return jwtAccount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JwtAccountAuthentication that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equal(jwtAccount, that.jwtAccount) && Objects.equal(jwt, that.jwt);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(super.hashCode(), jwtAccount, jwt);
    }
}

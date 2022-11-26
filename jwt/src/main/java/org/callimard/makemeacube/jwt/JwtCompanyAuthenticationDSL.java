package org.callimard.makemeacube.jwt;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JwtCompanyAuthenticationDSL extends AbstractHttpConfigurer<JwtCompanyAuthenticationDSL, HttpSecurity> {

    @Override
    public void configure(HttpSecurity http) {
        AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
        http.addFilterBefore(new JwtAuthenticationFilter(authenticationManager), UsernamePasswordAuthenticationFilter.class);
    }

    public static JwtCompanyAuthenticationDSL jwtAuthenticationDSL() {
        return new JwtCompanyAuthenticationDSL();
    }
}

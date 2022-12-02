package org.callimard.makemeacube.user_management;

import lombok.RequiredArgsConstructor;
import org.callimard.makemeacube.common.CommonConfiguration;
import org.callimard.makemeacube.common.api.ApiV1;
import org.callimard.makemeacube.jwt.JwtAuthenticationProvider;
import org.callimard.makemeacube.jwt.JwtCompanyAuthenticationDSL;
import org.callimard.makemeacube.jwt.JwtConfiguration;
import org.callimard.makemeacube.models.ModelsConfiguration;
import org.callimard.makemeacube.security.SecurityConfiguration;
import org.callimard.makemeacube.user_management.authentication.BasicAuthenticationProvider;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true)
@Import({JwtConfiguration.class, ModelsConfiguration.class, CommonConfiguration.class, SecurityConfiguration.class})
public class UserManagementConfiguration {

    // Variables.

    private final BasicAuthenticationProvider basicAuthenticationProvider;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;

    // Methods.

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.httpBasic()
                .and().anonymous()
                .and().cors().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().formLogin().disable()
                .logout().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers(HttpMethod.POST, ApiV1.AUTHENTICATION_URL + "/**").authenticated()
                .antMatchers(HttpMethod.GET, ApiV1.USERS_URL + "/makers").permitAll()
                .antMatchers(HttpMethod.POST, ApiV1.USERS_URL + "/basic-registration").anonymous()
                .antMatchers(HttpMethod.POST, ApiV1.USERS_URL + "/maker-registration").anonymous()
                .antMatchers(HttpMethod.GET, ApiV1.USERS_URL + "/**").authenticated()
                .antMatchers(HttpMethod.POST, ApiV1.USERS_URL + "/**").authenticated()
                .antMatchers(HttpMethod.PUT, ApiV1.USERS_URL + "/**").authenticated()
                .antMatchers(HttpMethod.DELETE, ApiV1.USERS_URL + "/**").authenticated()
                .anyRequest().denyAll();

        http.headers().xssProtection().and().contentSecurityPolicy("script-src 'self'");

        http.authenticationProvider(jwtAuthenticationProvider).authenticationProvider(basicAuthenticationProvider);

        http.apply(JwtCompanyAuthenticationDSL.jwtAuthenticationDSL());

        return http.build();
    }
}

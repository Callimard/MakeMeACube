package org.callimard.printmeacube.user_management;

import lombok.RequiredArgsConstructor;
import org.callimard.printmeacube.ApiV1;
import org.callimard.printmeacube.common.CommonConfiguration;
import org.callimard.printmeacube.entities.EntitiesConfiguration;
import org.callimard.printmeacube.jwt.JwtAuthenticationProvider;
import org.callimard.printmeacube.jwt.JwtCompanyAuthenticationDSL;
import org.callimard.printmeacube.jwt.JwtConfiguration;
import org.callimard.printmeacube.user_management.authentication.BasicAuthenticationProvider;
import org.springframework.boot.context.properties.ConfigurationProperties;
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
@ConfigurationProperties(prefix = "security")
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true)
@Import({JwtConfiguration.class, EntitiesConfiguration.class, CommonConfiguration.class})
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
                .antMatchers(HttpMethod.POST, ApiV1.USERS_URL + "/basic-registration").anonymous()
                .antMatchers(HttpMethod.POST, ApiV1.AUTHENTICATION_URL + "/**").authenticated()
                .anyRequest().denyAll();

        http.headers().xssProtection().and().contentSecurityPolicy("script-src 'self'");

        http.authenticationProvider(jwtAuthenticationProvider).authenticationProvider(basicAuthenticationProvider);

        http.apply(JwtCompanyAuthenticationDSL.jwtAuthenticationDSL());

        return http.build();
    }
}

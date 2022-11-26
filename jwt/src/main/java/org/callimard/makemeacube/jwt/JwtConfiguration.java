package org.callimard.makemeacube.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@AutoConfigurationPackage
@ComponentScan("org.callimard.makemeacube.jwt")
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "jwt")
public class JwtConfiguration {

    private String secret;

    private Integer defaultTimeExpiration = 15;

    @Bean
    public Algorithm hmac256() {
        return Algorithm.HMAC256(secret);
    }

    @Bean
    public JWTVerifier jwtVerifier(Algorithm algorithm) {
        return JWT.require(algorithm).build();
    }
}

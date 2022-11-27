package org.callimard.makemeacube.entities;

import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@AutoConfigurationPackage
@ComponentScan("org.callimard.makemeacube.entities")
@EnableJpaRepositories(enableDefaultTransactions = false)
public class EntitiesConfiguration {
}

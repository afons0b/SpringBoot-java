package jornadajava.spring_boot_trench.config;

import external.dependency.Connection;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@org.springframework.context.annotation.Configuration
@RequiredArgsConstructor
public class BeanConfiguration {
    private final ConnectionConfigurationProperties connectionConfigurationProperties;

    @Bean
    @Primary
    public Connection connectionMySql(){
        return new Connection(connectionConfigurationProperties.url(),
                connectionConfigurationProperties.username(),
                connectionConfigurationProperties.password());
    }

}

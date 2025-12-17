package jornadajava.spring_boot_trench.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "brasil-api")
public record BrasilApiConfigurationProperties(String baseUrl, String cepUri) {
}

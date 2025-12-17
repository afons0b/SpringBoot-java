package jornadajava.spring_boot_trench.config;

import jornadajava.spring_boot_trench.service.BrasilApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@RequiredArgsConstructor
public class RestClientConfiguration {
    private final BrasilApiConfigurationProperties brasilApiConfigurationProperties;
    private BrasilApiService service;
    @Bean(name = "brasilApiClient")
    public RestClient.Builder brasilApiRestClient(){
        return RestClient.builder().baseUrl(brasilApiConfigurationProperties.baseUrl());
    }
}

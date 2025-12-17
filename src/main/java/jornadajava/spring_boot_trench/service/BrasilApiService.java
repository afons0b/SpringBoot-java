package jornadajava.spring_boot_trench.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jornadajava.spring_boot_trench.config.BrasilApiConfigurationProperties;
import jornadajava.spring_boot_trench.exception.NotFoundException;
import jornadajava.spring_boot_trench.response.CepErrorResponse;
import jornadajava.spring_boot_trench.response.CepGetResponse;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class BrasilApiService {
    private final RestClient.Builder brasilApiClient;
    private final BrasilApiConfigurationProperties brasilApiConfigurationProperties;
    private final ObjectMapper mapper;

    public CepGetResponse findCep(String cep){
        return brasilApiClient.build()
                .get()
                .uri(brasilApiConfigurationProperties.cepUri(), cep)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                   var body =  response.getBody().readAllBytes();
                   var cepErrorResponse = mapper.readValue(body, CepErrorResponse.class);
                   throw new NotFoundException(cepErrorResponse.toString());
                })
                .body(CepGetResponse.class);
    }
}

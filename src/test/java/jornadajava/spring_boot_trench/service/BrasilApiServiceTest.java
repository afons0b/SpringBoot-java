package jornadajava.spring_boot_trench.service;

import jornadajava.spring_boot_trench.commons.CepUtils;
import jornadajava.spring_boot_trench.config.BrasilApiConfigurationProperties;
import jornadajava.spring_boot_trench.config.RestClientConfiguration;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestClient;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
@RestClientTest({BrasilApiService.class,
        BrasilApiConfigurationProperties.class,
        ObjectMapper.class,
        CepUtils.class,
        RestClientConfiguration.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BrasilApiServiceTest {

    @Autowired
    private BrasilApiService service;

    @Autowired
    @Qualifier("brasilApiClient")
    private RestClient.Builder brasilApiClientBuilder;

    @Autowired
    private MockRestServiceServer server;

    @Autowired
    private BrasilApiConfigurationProperties configuration;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private CepUtils cepUtils;

    @AfterEach
    void reset(){
        server.reset();
    }

    @Order(1)
    @Test
    @DisplayName("find ced returns cep get responde when succesful")
    void findcep() throws IOException {
        server = MockRestServiceServer.bindTo(brasilApiClientBuilder).build();
        var cep = "00000000";
        var cepGetResponse = cepUtils.cepGetResponse();
        var jsonResponse = mapper.writeValueAsString(cepGetResponse);
        var requestTo = MockRestRequestMatchers.requestToUriTemplate(configuration.baseUrl() + configuration.cepUri(), cep);
        var withSuccessful = MockRestResponseCreators.withSuccess(jsonResponse, MediaType.APPLICATION_JSON);
        server.expect(requestTo).andRespond(withSuccessful);

        Assertions.assertThat(service.findCep(cep)).isNotNull().isEqualTo(cepGetResponse);
    }

}
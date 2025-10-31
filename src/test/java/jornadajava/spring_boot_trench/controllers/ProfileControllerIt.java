package jornadajava.spring_boot_trench.controllers;

import jornadajava.spring_boot_trench.config.TestcontainersConfiguration;
import jornadajava.spring_boot_trench.request.ProfilePostRequest;
import jornadajava.spring_boot_trench.response.ProfileGetResponse;
import jornadajava.spring_boot_trench.response.ProfilePostResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
@Transactional
@Import(TestcontainersConfiguration.class)
class ProfileControllerIt {
    private static final String URL = "/v1/profile";
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("GET")
    //2
    @Sql("/sql/init_2_profiles.sql")
    @Order(1)
    void findAll_returndAllProfiles_WhenSuccessful() throws Exception{
        //aqui estamos definindo o tipo de objeto que estamos esperando como resposta
       var typeReference = new ParameterizedTypeReference<List<ProfileGetResponse>>(){

        };
       //aqui estamos executando o metodo, apontando pra nossa url,
        //o tipo do metodo(GET), o corpo da requisição(null) e o tipo de objeto que estamos esperando
       var responseEntity = restTemplate.exchange(URL, HttpMethod.GET, null, typeReference);

       //assertions:
       org.assertj.core.api.Assertions.assertThat(responseEntity).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responseEntity.getBody()).isNotNull().doesNotContainNull();

        responseEntity
                .getBody()
                .forEach(profileGetResponse -> Assertions.assertThat(profileGetResponse).hasNoNullFieldsOrProperties());
    }

    @Test
    @DisplayName("GET")
    @Order(2)
    void findAll_returns_emptyList(){
        var typeReference = new ParameterizedTypeReference<List<ProfileGetResponse>>(){

        };
        var responseEntity = restTemplate.exchange(URL, HttpMethod.GET, null, typeReference);

        org.assertj.core.api.Assertions.assertThat(responseEntity).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responseEntity.getBody()).isNotNull().isEmpty();

    }

    @Test
    @DisplayName("POST")
    @Order(3)
    void saves_aNew_object(){
        var profileToSave = ProfilePostRequest.builder()
                .name("ADMIN_TESTE")
                .description("restTemplate teste").build();

        var profileHttpEntity = new HttpEntity<>(profileToSave);
        var responseEntity = restTemplate
                .exchange(URL, HttpMethod.POST, profileHttpEntity, ProfilePostResponse.class);

        org.assertj.core.api.Assertions.assertThat(responseEntity).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(responseEntity.getBody()).isNotNull().hasNoNullFieldsOrProperties();

        ProfilePostResponse postResponse = responseEntity.getBody();

        Assertions.assertThat(postResponse.getId()).isNotNull();
        Assertions.assertThat(postResponse.getName()).isEqualTo("ADMIN_TESTE");
    }
}

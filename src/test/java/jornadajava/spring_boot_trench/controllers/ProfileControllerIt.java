package jornadajava.spring_boot_trench.controllers;

import jornadajava.spring_boot_trench.config.TestRestTemplateConfig;
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
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import java.util.List;


//essa anotação gera uma porta aleatoria para que possamos rodar o teste container
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//ativamos
@ActiveProfiles("test")
//aqui adicionamos um teste de containers, por que?
//sem isso, eu teria que usar o banco H2, é rapido mas é "falso"
//o mysql tem comando que o h2 nao tem, testar no h2 e rodar em produção no sql
//é pedir pra dar bugs surpresa
//entao subimos um container cm o banco sql para simular melhor
@Import({TestcontainersConfiguration.class, TestRestTemplateConfig.class})
@DirtiesContext
class ProfileControllerIt {
    private static final String URL = "/v1/profile";
    //eu preciso que alguem faça requisiçoes http,
    //mas como isso é um teste entao useu o test rest template
    //o test rest template é como se fosse um postman so que rodando dentro do codigo java
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("GET")
    //2
    @Sql(value = "/sql/init_one_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
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
    @Sql(value = "/sql/init_only_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
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
    @DisplayName("POST - Cria perfil com sucesso")
    @Sql(value = "/sql/init_only_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Order(3)
    void saves_aNew_object(){
        var profileToSave = ProfilePostRequest.builder()
                .name("ADMIN_TESTE")
                .description("restTemplate teste").build();

        //Busca o Token antes de enviar
        HttpHeaders headers = getCsrfHeaders();

        //Envelopa o Objeto + Headers (Token)
        var profileHttpEntity = new HttpEntity<>(profileToSave, headers);

        var responseEntity = restTemplate
                .exchange(URL, HttpMethod.POST, profileHttpEntity, ProfilePostResponse.class);

        //assertions
        org.assertj.core.api.Assertions.assertThat(responseEntity).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(responseEntity.getBody()).isNotNull().hasNoNullFieldsOrProperties();

        ProfilePostResponse postResponse = responseEntity.getBody();
        Assertions.assertThat(postResponse.getId()).isNotNull();
        Assertions.assertThat(postResponse.getName()).isEqualTo("ADMIN_TESTE");
    }

    //Esse método vai no /csrf, pega o cookie e o token e devolve os headers prontos
    private HttpHeaders getCsrfHeaders() {
        //Chama o endpoint /csrf
        ResponseEntity<String> csrfResponse = restTemplate.exchange("/csrf", HttpMethod.GET, null, String.class);

        //Pega os Cookies (Sessão + Token)
        List<String> cookies = csrfResponse.getHeaders().get(HttpHeaders.SET_COOKIE);

        //Valida se veio cookie
        if (cookies == null || cookies.isEmpty()) {
            // Tenta uma estratégia de fallback simples caso a lista venha nula
            return new HttpHeaders();
        }

        //Junta os cookies numa string só
        String cookieHeaderValue = String.join("; ", cookies);

        //Extrai o valor do XSRF-TOKEN
        String xsrfToken = cookies.stream()
                .filter(c -> c.contains("XSRF-TOKEN="))
                .findFirst()
                .map(c -> c.split("XSRF-TOKEN=")[1].split(";")[0])
                .orElse("");

        //Monta o Header de volta
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(HttpHeaders.COOKIE, cookieHeaderValue);
        headers.add("X-XSRF-TOKEN", xsrfToken);

        return headers;
    }
}

package jornadajava.spring_boot_trench.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import jornadajava.spring_boot_trench.commons.FileUtils;
import jornadajava.spring_boot_trench.commons.UserUtils;
import jornadajava.spring_boot_trench.config.TestcontainersConfiguration;
import jornadajava.spring_boot_trench.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Import(TestcontainersConfiguration.class)
@ActiveProfiles("test")
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
@Sql(value = "/sql/init_one_login_user.sql")
@Sql(value = "/sql/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
public class UserControllerRestAssuredIT {
    private static final String URL = "/v1/user";
    @Autowired
    private UserUtils userUtils;
    @Autowired
    private FileUtils fileUtils;
    @LocalServerPort
    private int port;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository repository;

    @BeforeEach
    void setUrl(){

        RestAssured.reset();

        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;

        RestAssured.authentication = RestAssured.preemptive()
                .basic("joca@teste.academy", "jokinha");

        var csrfResponse = RestAssured.given()
                .get("/csrf")
                .then()
                .statusCode(200)
                .extract();

        String cookieBruto = csrfResponse.header("Set-cookie");
        String token = csrfResponse.path("token");
        String headerName = csrfResponse.path("headerName");

        RestAssured.requestSpecification = new RequestSpecBuilder()
                .addHeader(headerName, token)
                .addHeader("Cookie", cookieBruto)
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .build();
    }

    @Test
    @DisplayName("GET v1/user retorna a lista de todos os usuarios")
    @Sql(value = "/sql/init_three_login_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Order(1)
    void list_all_users_when_successful() throws IOException {
        var response = fileUtils.readResourceFile("user/get-user-list-200.json");
        //para nao ter problema de comparação entre arquivos, vou transformar uma lista de string
        //para objetos
        List<Map<String, Object>> listaEsperada = objectMapper
                .readValue(response, new TypeReference<List<Map<String, Object>>>(){});
        RestAssured.given()
                .when()
                .get(URL)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("", Matchers.equalTo(listaEsperada))
                .log().all();
    }

    @Test
    @DisplayName("GET v1/user/filter retorna uma lista de usuarios de mesmo nome")
    @Sql(value = "/sql/init_users_with_same_name.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Order(2)
    void list_users_with_same_name() throws JsonProcessingException {
        var response = fileUtils.readResourceFile("user/get-user-filter-list-200.json");

        List<Map<String, Object>> listaEsperada = objectMapper
                .readValue(response, new TypeReference<List<Map<String, Object>>>() {});
        RestAssured.given()
                .queryParam("name", "Joca")
                .when()
                .get(URL)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("", Matchers.equalTo(listaEsperada))
                .log().all();
    }

    @Test
    @DisplayName("GET v1/user/id retorna um usuario passado um id")
    @Sql(value = "/sql/init_three_login_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Order(3)
    void return_a_user_given_id() throws JsonProcessingException {
        var response = fileUtils.readResourceFile("user/get-user-by-id-200.json");
        Map<String, Object> userById = objectMapper
                .readValue(response, new TypeReference<Map<String, Object>>() {});
        RestAssured.given()
                .when()
                .get(URL + "/{id}", 1)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("", Matchers.equalTo(userById))
                .log().all();
    }

    @Test
    @DisplayName("POST v1/user salva um usuario no banco")
    @Sql(value = "/sql/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Order(4)
    void save_a_new_user() throws IOException {
        var request = fileUtils.readResourceFile("user/post-request-user-201.json");
        var response = fileUtils.readResourceFile("user/post-response-user-201.json");

        Map<String, Object> expectedMap = objectMapper
                .readValue(response, new TypeReference<Map<String, Object>>() {});

        Map<String, Object> mapReal = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post(URL)
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .as(new TypeRef<Map<String, Object>>() {});

        Number idNumber = (Number) mapReal.get("id");
        Long idGerado = idNumber.longValue();

        var userdb = repository.findById(idGerado)
                .orElseThrow(() -> new RuntimeException("Usuario nao encontrado"));

        Assertions.assertThat(mapReal.get("id")).isNotNull();
        Assertions.assertThat(userdb.getRoles()).isEqualTo("USER");

        mapReal.remove("id");
        expectedMap.remove("id");

        Assertions.assertThat(mapReal).isEqualTo(expectedMap);
    }

    @Test
    @DisplayName("DELETE v1/user atualiza um usuario com id passado")
    @Order(5)
    void delete_a_user_given_id(){
        RestAssured.given()
                .when()
                .delete(URL + "/{id}", 1)
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        var userOptional = repository.findById(1L);
        Assertions.assertThat(userOptional).isEmpty();
    }

    @Test
    @DisplayName("PUT v1/user atualiza usuário e retorna o objeto atualizado")
    @Order(5)
    void update_User_WhenSuccessful() throws IOException {

        var request = fileUtils.readResourceFile("user/put-request-user-200.json");
        var response = fileUtils.readResourceFile("user/put-response-user-200.json");

        // Transforma o arquivo de resposta em Map
        Map<String, Object> mapEsperado = objectMapper
                .readValue(response, new TypeReference<Map<String, Object>>() {});

        Map<String, Object> mapReal = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .put(URL + "/{id}", 1)
                .then()
                .statusCode(HttpStatus.OK.value()) // Agora esperamos 200 OK
                .extract()
                .as(new TypeRef<Map<String, Object>>() {}); // Extrai o JSON de volta

        var userNoBanco = repository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Joca sumiu do banco!"));

        // Confere se no banco o nome mudou mesmo
        Assertions.assertThat(userNoBanco.getName()).isEqualTo("Joca Atualizado");

        mapReal.remove("id");
        mapEsperado.remove("id");
        // Se o arquivo response já tem id 1, nem precisa remover.
        // Mas se quiser garantir que não falhe por roles extras, removemos roles.

        // Compara se o JSON de resposta bate com o esperado
        Assertions.assertThat(mapReal).isEqualTo(mapEsperado);
    }

}

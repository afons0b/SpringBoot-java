package jornadajava.spring_boot_trench.controllers;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import jornadajava.spring_boot_trench.commons.FileUtils;
import jornadajava.spring_boot_trench.commons.UserUtils;
import jornadajava.spring_boot_trench.config.TestcontainersConfiguration;
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

    @BeforeEach
    void setUrl(){
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
}

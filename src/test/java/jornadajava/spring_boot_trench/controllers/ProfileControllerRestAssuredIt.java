package jornadajava.spring_boot_trench.controllers;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import jornadajava.spring_boot_trench.commons.FileUtils;
import jornadajava.spring_boot_trench.config.TestcontainersConfiguration;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;


//essa anotação gera uma porta aleatoria para que possamos rodar o teste container
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Sql(value = "/sql/init_one_login_user.sql")
@Sql(value = "/sql/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
//pq usar sql merge? imagine que minha classe de teste tenha 500 metodos para testar,
// escrever @Sql e o arquivo em cada metodo iria repetir toda vez e tomar muito tempo,
//entao adicionamos ele so uma vez cm o sql merge
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
@ActiveProfiles("test")
@Import(TestcontainersConfiguration.class)
class ProfileControllerRestAssuredIt {
    private static final String URL = "/v1/profile";
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private FileUtils fileUtils;
    @LocalServerPort
    private int port;

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;

        //aqui estamos criando uma autenticação para os testes de post, put e delete
        //como o spring security tranca tudo, ate mesmo os testes, ele vai precisar de autenticação tbm
        //efine a Autenticação
        RestAssured.authentication = RestAssured.preemptive()
                .basic("joca@teste.academy", "jokinha");

        // 2. Busca o Token CSRF
        var csrfResponse = RestAssured.given()
                .log().all() //Adicionei log pra ver a resposta crua
                .get("/csrf")
                .then()
                .statusCode(200)
                .extract();

        //Extrai os dados com segurança
        String token = csrfResponse.path("token");
        String headerName = csrfResponse.path("headerName");

        //Tenta pegar o cookie bruto
        String cookieBruto = csrfResponse.header("Set-Cookie");

        //Se não achou "Set-Cookie", tenta pegar o JSESSIONID específico
        if (cookieBruto == null) {
            String jsessionid = csrfResponse.cookie("JSESSIONID");
            if (jsessionid != null) {
                cookieBruto = "JSESSIONID=" + jsessionid;
            }
        }

        System.out.println("--- DEBUG ---");
        System.out.println("Token: " + token);
        System.out.println("HeaderName: " + headerName);
        System.out.println("Cookie: " + cookieBruto);
        System.out.println("-------------");

        // 4. Configura o Builder
        var builder = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON);

        // Só adiciona se não for nulo! (Isso evita o crash)
        if (token != null && headerName != null) {
            builder.addHeader(headerName, token);
        }

        // Só adiciona o cookie se ele existir
        if (cookieBruto != null) {
            builder.addHeader("Cookie", cookieBruto);
        }

        RestAssured.requestSpecification = builder.build();
    }

    @Test
    @DisplayName("GET")
    @Sql(value = "/sql/init_2_profiles.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Order(1)
    void findAll_returndAllProfiles_WhenSuccessful() throws Exception{
        var response = fileUtils.readResourceFile("profile/get-all-profiles-200.json");
        RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .get(URL)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(Matchers.equalTo(response))
                .log().all();
    }

    @Test
    @DisplayName("POST")
    @Sql(value = "/sql/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Order(2)
    void saveMethod_Create_A_Profile_WhemSucessful() throws Exception{
        var request = fileUtils.readResourceFile("profile/post-request-200.json");
        var response = fileUtils.readResourceFile("profile/post-response-200.json");

        RestAssured.given().contentType(ContentType.JSON).accept(ContentType.JSON)
                .body(request)
                .when()
                .post(URL)
                .then().statusCode(HttpStatus.CREATED.value())
                .body("id", Matchers.notNullValue())
                .body("description", Matchers.equalTo("Gerente de Projetos com acesso limitado"))
                .body("name", Matchers.equalTo("MANAGER")).log().all();
    }

}

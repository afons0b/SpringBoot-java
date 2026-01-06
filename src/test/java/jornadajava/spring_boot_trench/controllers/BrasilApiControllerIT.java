package jornadajava.spring_boot_trench.controllers;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import jornadajava.spring_boot_trench.commons.FileUtils;
import jornadajava.spring_boot_trench.config.TestcontainersConfiguration;
import jornadajava.spring_boot_trench.config.RestAssuredConfig;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.io.IOException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({TestcontainersConfiguration.class, RestAssuredConfig.class})
@Sql(value = "/sql/init_one_login_user.sql")
@ActiveProfiles("test")
@AutoConfigureWireMock(port = 0, files = "classpath:/wiremock/brasil-api/cep", stubs = "classpath:/wiremock/brasil-api/cep/mappings")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BrasilApiControllerIT {
    private static final String URL = "v1/brasil-api/cep";

    @Autowired
    private FileUtils fileUtils;
    @Autowired
    @Qualifier(value = "requestSpecificationRegularUser")
    private RequestSpecification requestSpecificationRegularUser;


    @BeforeEach
    void setUrl(){
        RestAssured.requestSpecification = requestSpecificationRegularUser;
    }

    @Order(1)
    @Test
    @DisplayName("find ced returns cep get responde when succesful")
    void findcep() throws IOException {
        var cep = "00000000";
        var response = fileUtils.readResourceFile("brasil-api/cep/expected-get-cep-response-200.json");
        RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .when()
                .get(URL+"/{cep}", cep)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(Matchers.equalTo(response))
                .log().all();
    }
}

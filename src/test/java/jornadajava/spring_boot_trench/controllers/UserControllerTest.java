package jornadajava.spring_boot_trench.controllers;

import jornadajava.spring_boot_trench.domain.User;
import jornadajava.spring_boot_trench.mapper.UserMapperImpl;
import jornadajava.spring_boot_trench.repository.UserData;
import jornadajava.spring_boot_trench.repository.UserHardCodedRepository;
import jornadajava.spring_boot_trench.repository.UserRepository;
import jornadajava.spring_boot_trench.service.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@WebMvcTest(controllers = UserController.class)
@ComponentScan(basePackages = {"jornadajava.spring_boot_trench"})
@ActiveProfiles("test")
@Import({UserMapperImpl.class, UserService.class, UserHardCodedRepository.class, UserData.class})
class UserControllerTest {
    @Autowired
    //o MockMvc é como se fosse um postman mas emulado no teste, um postman de mentira
    private MockMvc mockMvc;

    @MockitoBean
    private UserData userData;
    private final List<User> userList = new ArrayList<>();

    @MockitoBean
    private UserRepository userRepository;

    @Autowired
    private ResourceLoader resourceLoader;

    @BeforeEach
    void init(){
        var user1 = User.builder().id(1L).name("Afonso").idade(24).lastName("Braga").email("afonsobaraga@email.com").build();
        var user2 = User.builder().id(2L).name("Iuri").idade(23).lastName("Maciel").email("Iurimaciel@email.com").build();
        var user3 = User.builder().id(3L).name("Tales").idade(20).lastName("Ribeiro").email("talesribeiro@email.com").build();
        var user4 = User.builder().id(4L).name("Antonio").idade(38).lastName("Hidaka").email("antoniohidaka@email.com").build();
        var user5 = User.builder().id(5L).name("Afonso").idade(24).lastName("Braga").email("afonsobaraga@email.com").build();
        userList.addAll(List.of(user1, user2, user3, user4, user5));
    }

    private String readResourceLoader(String fileName) throws IOException {
        //aqui estamos buscando o arquivo com o json para comparação,
        //o resourceloader usa o classpath para buscar o arquivo passado.
        // Resource loade esta dando o caminho pelo classpath e o get file ta encontrando ele
        var file = resourceLoader.getResource("classpath:%s".formatted(fileName)).getFile();
        //o readAllBytes le o json em dados brutos, literalmente bytes e dps é retornado como string para ser lido
        return new String(Files.readAllBytes(file.toPath()));
    }

    @Test
    @DisplayName("getAll deve retornar lista de DTOs")
    @Order(1)
    void getAll_retornaListaDeDtos() throws Exception {
        BDDMockito.when(userRepository.findAll()).thenReturn(userList);
        var response = readResourceLoader("user/get-full-list.200.json");

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/user"))
                //andDo e andExpect sao basicamente assertions,
                // esse primeiro diz para printar tudo no terminal
                .andDo(MockMvcResultHandlers.print())
                //o segundo espera que o status seja 200 ok
                .andExpect(MockMvcResultMatchers.status().isOk())
                //pra uma explicaçao melhor, o teste emula um get all,
                // ou seja alguem esta dando um get all e vai receber um json completo,
                //entao "esse assertion" compara o json emulado com o json do arquivo response
                //e verifica se ambos sao iguais, se for o teste passa
                .andExpect(MockMvcResultMatchers.content().json(response));

    }

    @Test
    @DisplayName("GET V1/user?nome=Afonso retorna uma lista de objetos com esse nome")
    @Order(2)
    void get_name_filtered() throws Exception{
        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        var response = readResourceLoader("user/get-name-list-200.json");
        var name = "Afonso";

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/user/filter").param("name", name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/user/1 retorna um objeto com id passado")
    @Order(3)
    void findById_ReturnSeriePorId_quandoSucesso() throws Exception {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);
        var id = 1L;
        var response = readResourceLoader("user/get-user-by-id.json");

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/user/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("POST v1/user/ salva um novo objeto no banco")
    @Order(4)
    void save_method() throws Exception {

        var request = readResourceLoader("user/save-request-user-201.json");

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/user")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Carlos"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("dev"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.idade").value(30))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("carlosdev@email.com"));
    }

    @Test
    @DisplayName("metodo para testar atualizar um objeto usando DTO")
    @Order(5)
    void update_method() throws Exception {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);
        var request = readResourceLoader("user/put-request-user-201.json");
        var id = 1L;

        mockMvc.perform(MockMvcRequestBuilders.put("/v1/user/{id}", id)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())

                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("eduardo"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("devsenior"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.idade").value(60))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("eduardodev@email.com"));
    }

    @Test
    @DisplayName("metodo para deletar um objeto pelo id")
    @Order(5)
    void deleteById_testMethod() throws Exception {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        var id = userList.getFirst().getId();

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/user/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    //parametrized test serve para testar o mesmo metodo mas com diferentes entradas,
    //estamos testando a validação de campos diferentes usando o mesmo metodo
    @ParameterizedTest
    @MethodSource("postMethodBadRequest")
    @DisplayName("POST v1/user/ deve retornar um bad request quando os campos estiverem vazios")
    @Order(6)
    //no metodo teremos um parametro string e uma lista
    void save_method_returns_bad_request(String fileName, List<String> erros) throws Exception {

        var request = readResourceLoader("user/%s".formatted(fileName));

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post("/v1/user")
                        .content(request).contentType(MediaType.APPLICATION_JSON))

                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();
        var resolvedException = mvcResult.getResolvedException();

        Assertions.assertThat(resolvedException).isNotNull();

        //verificando se as 3 mensagens de alerta estão no resolved excepion
        Assertions.assertThat(resolvedException.getMessage()).contains(erros);
    }

    private static Stream<Arguments> postMethodBadRequest(){
        var nameRequiredError = "campo nome é necessario";
        var lastNameRequiredError = "o campo lastName é necessario";
        var emailRequiredError = "campo email é necessario";
        var emailInvalidoError = "o email inserido é invalido";

        var allErrors = List.of(nameRequiredError, lastNameRequiredError, emailRequiredError);
        var emailError = Collections.singletonList(emailInvalidoError);

        return Stream.of(
                Arguments.of("save-request-user-empty-fields-400.json", allErrors),
                Arguments.of("save-request-user-blank-fields-400.json", allErrors),
                Arguments.of("post-user-request-email-invalid-400.json", emailError)
        );
    }

}
package jornadajava.spring_boot_trench.repository;

import jornadajava.spring_boot_trench.domain.User;
import jornadajava.spring_boot_trench.response.UserGetResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.control.MappingControl;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserRepositoryTest {
    @Mock
    private UserData userData;
    private final List<User> userList = new ArrayList<>();

    @InjectMocks
    private UserRepository repository;

    @BeforeEach
    void init(){
        var user1 = User.builder().id(1L).name("Afonso").idade(24).lastName("Braga").email("afonsobaraga@email.com").build();
        var user2 = User.builder().id(2L).name("Iuri").idade(23).lastName("Maciel").email("Iurimaciel@email.com").build();
        var user3 = User.builder().id(3L).name("Tales").idade(20).lastName("Ribeiro").email("talesribeiro@email.com").build();
        var user4 = User.builder().id(4L).name("Antonio").idade(38).lastName("Hidaka").email("antoniohidaka@email.com").build();
        var user5 = User.builder().id(5L).name("Afonso").idade(24).lastName("Braga").email("afonsobaraga@email.com").build();
        userList.addAll(List.of(user1, user2, user3, user4, user5));

    }

    @Test
    @DisplayName("findAll deveria retornar uma lista de series")
    @Order(1)
    void findAll_returnAllSeries_WhenSuccessful(){
        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        var resultado = repository.listAll();

        Assertions.assertThat(resultado).hasSize(5);
    }

    @Test
    @DisplayName("findById deveria retornar um nome pelo Id")
    @Order(2)
    void findById_returningId(){
        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        // pegaremos uma serie para testar um metodo, para isso posso usar o getFirst,
        // assim nao preciso ter controle dos dados
        var expectedUser = userData.getUsers().getFirst();

        // com o objeto em maos, testamos o metodo
        var result = repository.findById(expectedUser.getId());

        // agora farei os assertions,
        // basicamente afirmações que vao me confirmar se o teste ira passar ou nao

        Assertions.assertThat(result).isNotNull();

        Assertions.assertThat(result)
                .isPresent()
                .get()
                .isEqualTo(expectedUser);
    }

    @Test
    @DisplayName("findByName deveria retornar uma lista de nomes")
    @Order(3)
    void findByName_return_List_Of_Names_When_Successul(){
        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        var nomesEsperados = userList.getFirst();

        var resultado = repository.findByName(nomesEsperados.getName());

        //verificando se a lista nao é nula
        Assertions.assertThat(resultado).isNotNull();

        //verificando se a lista de possui 2 objetos
        Assertions.assertThat(resultado).hasSize(2);
    }

    @Test
    @DisplayName("save deveria criar e salvar um novo objeto")
    @Order(4)
    void saveMethod_shouldCreate_andSaved_newObject(){
        BDDMockito.when(userData.getUsers()).thenReturn(userList);
        BDDMockito.when(userData.getNewId()).thenReturn(4L);

        User newUser = User.builder()
                .id(null)
                .name("fulano")
                .idade(20)
                .lastName("ciclano")
                .email("fulano@email.com").build();

        var savedUser = repository.save(newUser);

        Assertions.assertThat(userList).hasSize(6);

        Assertions.assertThat(savedUser.getId()).isNotNull();

        Assertions.assertThat(savedUser.getId()).isEqualTo(4L);
    }

    @Test
    @DisplayName("metodo delete remove uma serie")
    @Order(5)
    void delete_method_FindById(){
        BDDMockito.when(userData.getUsers()).thenReturn(userList);

       var userToDelete = userList.getFirst();
       repository.deleteById(userToDelete.getId());

       Assertions.assertThat(userList).hasSize(4);

       Assertions.assertThat(userList).doesNotContain(userToDelete);
    }

    @Test
    @DisplayName("metodo update deve atualizar um usuario")
    @Order(6)
    void methodUpdate_to_update_a_TVSerie(){
        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        var updatedUser = User.builder()
                .id(1L)
                .name("eduardo")
                .lastName("chefe")
                .idade(58)
                .email("eduardousti@email.com").build();

        var resultado = repository.update(updatedUser);

        Assertions.assertThat(resultado.getId()).isEqualTo(1L);

        Assertions.assertThat(resultado.getName()).isEqualTo("eduardo");

        Assertions.assertThat(userList).contains(resultado);
    }


}
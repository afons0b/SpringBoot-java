package jornadajava.spring_boot_trench.repository;

import jornadajava.spring_boot_trench.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
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
        userList.addAll(List.of(user1, user2, user3, user4));
    }

    @Test
    @DisplayName("findById deveria retornar uma serie pelo Id")
    @Order(1)
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

}
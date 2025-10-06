package jornadajava.spring_boot_trench.service;

import jornadajava.spring_boot_trench.domain.Serie;
import jornadajava.spring_boot_trench.domain.User;
import jornadajava.spring_boot_trench.mapper.UserMapper;
import jornadajava.spring_boot_trench.repository.UserRepository;
import jornadajava.spring_boot_trench.response.SerieGetResponse;
import jornadajava.spring_boot_trench.response.UserGetResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repository;
    private final List<User> userListDomain = new ArrayList<>();
    private final List<UserGetResponse> userGetResponseList = new ArrayList<>();

    @Mock
    private UserMapper mapper;

    @InjectMocks
    private UserService service;

    @BeforeEach
    void init(){
        var user1 = User.builder().id(1L).name("Afonso").idade(24).lastName("Braga").email("afonsobaraga@email.com").build();
        var user2 = User.builder().id(2L).name("Iuri").idade(23).lastName("Maciel").email("Iurimaciel@email.com").build();
        var user3 = User.builder().id(3L).name("Tales").idade(20).lastName("Ribeiro").email("talesribeiro@email.com").build();
        var user4 = User.builder().id(4L).name("Antonio").idade(38).lastName("Hidaka").email("antoniohidaka@email.com").build();
        var user5 = User.builder().id(5L).name("Afonso").idade(24).lastName("Braga").email("afonsobaraga@email.com").build();
        userListDomain.addAll(List.of(user1, user2, user3, user4, user5));

        var userResponse1 = UserGetResponse.builder().id(1L).name("Afonso").idade(24).lastName("Braga").email("afonsobaraga@email.com").build();
        var userResponse2 = UserGetResponse.builder().id(2L).name("Iuri").idade(23).lastName("Maciel").email("Iurimaciel@email.com").build();
        var userResponse3 = UserGetResponse.builder().id(3L).name("Tales").idade(20).lastName("Ribeiro").email("talesribeiro@email.com").build();
        var userResponse4 = UserGetResponse.builder().id(4L).name("Antonio").idade(38).lastName("Hidaka").email("antoniohidaka@email.com").build();
        var userResponse5 = UserGetResponse.builder().id(5L).name("Afonso").idade(24).lastName("Braga").email("afonsobaraga@email.com").build();
        userGetResponseList.addAll(List.of(userResponse1, userResponse2, userResponse3, userResponse4, userResponse5));
    }


    @Test
    @DisplayName("getAll deve retornar lista de DTOs")
    @Order(1)
    void getAll_comForLoop_retornaListaDeDtos(){
        BDDMockito.when(repository.listAll()).thenReturn(userListDomain);
        //aqui percorremos ambas as listas e depois coletando cada objeto a cada volta
        for (int i = 0; i < userListDomain.size(); i++){
            User userDomain = userListDomain.get(i);
            UserGetResponse userResponse = userGetResponseList.get(i);

            // e "ensinamos" o mapper a fazer o mapeamento
            BDDMockito.when(mapper.toUserGetResponse(userDomain)).thenReturn(userResponse);
        }

        List<UserGetResponse> resultado = service.getAll();

        Assertions.assertThat(resultado).isNotNull();

        Assertions.assertThat(resultado).isEqualTo(userGetResponseList);

        Assertions.assertThat(resultado).hasSize(5);
    }

    @Test
    @DisplayName("metodo findByName deve retornar uma lista de nomes usando como referencia o parametro passado")
    @Order(2)
    void findByName_deveRetornar_umaLista(){
        //como nao chamamos
        List<User> usersDomain = List.of(userListDomain.get(0), userListDomain.get(4));
        List<UserGetResponse> usersResponse = List.of(userGetResponseList.get(0), userGetResponseList.get(4));

        BDDMockito.when(repository.findByName("Afonso")).thenReturn(usersDomain);

        // um loop para percorrer todos os objetos da lista usersDomain
        for (int i = 0; i < usersDomain.size(); i++){
            //para cada volta na lista o mapper transforma em DTO o objeto lido
            BDDMockito.when(mapper.toUserGetResponse(usersDomain.get(i)))
                    .thenReturn(usersResponse.get(i));
        }

        var resultado = service.findByName("Afonso");

        Assertions.assertThat(resultado).hasSize(2);

        Assertions.assertThat(resultado).isEqualTo(usersResponse);
    }

    @Test
    @DisplayName("metodo para buscar um objeto pelo id")
    @Order(3)
    void findById_deveRetornar_umObjeto_peloId(){

        User user = userListDomain.getFirst();
        UserGetResponse userResponse = userGetResponseList.getFirst();

        //ensinando ao mockito devolver um optional do user
        BDDMockito.when(repository.findById(user.getId())).thenReturn(Optional.of(user));
        //ensinando ao mockito a transformar para dto, enquando chamamos o mapper dizemos a ele retornar o userResponse
        BDDMockito.when(mapper.toUserGetResponse(user)).thenReturn(userResponse);

        UserGetResponse resultado = service.findById(user.getId());

        Assertions.assertThat(resultado).isEqualTo(userResponse);
    }

}
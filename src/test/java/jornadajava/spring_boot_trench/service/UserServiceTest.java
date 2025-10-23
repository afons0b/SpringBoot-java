package jornadajava.spring_boot_trench.service;

import jornadajava.spring_boot_trench.domain.User;
import jornadajava.spring_boot_trench.mapper.UserMapper;
import jornadajava.spring_boot_trench.repository.UserHardCodedRepository;
import jornadajava.spring_boot_trench.repository.UserRepository;
import jornadajava.spring_boot_trench.request.UserPostRequest;
import jornadajava.spring_boot_trench.request.UserPutRequest;
import jornadajava.spring_boot_trench.response.UserGetResponse;
import jornadajava.spring_boot_trench.response.UserPostResponse;
import jornadajava.spring_boot_trench.response.UserPutResponse;
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
import java.util.stream.IntStream;

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
        BDDMockito.when(repository.findAll()).thenReturn(userListDomain);

        IntStream.range(0, userListDomain.size())
                .forEach(i->{var userDomain = userListDomain.get(i);
                    var responseList = userGetResponseList.get(i);
                BDDMockito.when(mapper.toUserGetResponse(userDomain)).thenReturn(responseList);
                });
        var result = service.findAll();

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result).isEqualTo(userGetResponseList);
        Assertions.assertThat(result).hasSize(5);
    }

    @Test
    @DisplayName("metodo findByName deve retornar uma lista de nomes usando como referencia o parametro passado")
    @Order(2)
    void findByName_deveRetornar_umaLista(){
        //como nao chamamos
        List<User> usersDomain = List.of(userListDomain.get(0), userListDomain.get(4));
        List<UserGetResponse> usersResponse = List.of(userGetResponseList.get(0), userGetResponseList.get(4));

        BDDMockito.when(repository.findByNameIgnoreCase("Afonso")).thenReturn(usersDomain);

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
        //como o mapper tbm esta mockado, temos q ensinar o mockito a mapear para dto
        //ensinando ao mockito a transformar para dto, enquando chamamos o mapper dizemos a ele retornar o userResponse
        BDDMockito.when(mapper.toUserGetResponse(user)).thenReturn(userResponse);

        UserGetResponse resultado = service.findById(user.getId());

        Assertions.assertThat(resultado).isEqualTo(userResponse);
    }

    @Test
    @DisplayName("metodo para deletar um objeto pelo id")
    @Order(4)
    void deleteById_testMethod(){

        User userToDelete = userListDomain.getFirst();

        BDDMockito.when(repository.findById(userToDelete.getId()))
                .thenReturn(Optional.of(userToDelete));

        service.delete(userToDelete.getId());

        BDDMockito.verify(repository, BDDMockito.times(1)).delete(userToDelete);
    }

    @Test
    @DisplayName("metodo para salvar um objeto na lista")
    @Order(5)
    void save_method_test(){

        //chegada do POST request
        UserPostRequest postRequest = UserPostRequest.builder()
                .name("teste")
                .lastName("service")
                .idade(40)
                .email("testeservice@gmail.com").build();

        //POST request no banco
        User userDomain = User.builder()
                .id(4L)
                .name("teste")
                .lastName("service")
                .idade(40)
                .email("testeservice@gmail.com").build();

        //retorno do user domain como dto post response
        //postResponse era pra ter o id
        //no futuro sera feita uma refatoração dos testes e da classe como um todo
        UserPostResponse postResponse = UserPostResponse.builder().name("teste").lastName("service").idade(40).email("testeservice@gmail.com").build();

        BDDMockito.when(mapper.toUser(postRequest)).thenReturn(userDomain);

        BDDMockito.when(repository.save(userDomain)).thenReturn(userDomain);

        BDDMockito.when(mapper.toUserPostResponse(userDomain)).thenReturn(postResponse);

        UserPostResponse response = service.saveUser(postRequest);

        Assertions.assertThat(response).isEqualTo(postResponse);
        Assertions.assertThat(response.getId()).isEqualTo(userDomain.getId());
    }

    @Test
    @DisplayName("metodo para atualizar um objeto usando DTO")
    @Order(7)
    void update_method(){
        //chegada do putRequest
        UserPutRequest putRequest = UserPutRequest.builder()
                .name("update")
                .lastName("method")
                .idade(26)
                .email("emailatualizado@gmail.com").build();

        //pegamos o objeto a ser atualizado
        User originalUser = userListDomain.getFirst();

        //fazemos agora o o response do update para o cliente
        UserPutResponse putResponse = UserPutResponse.builder()
                .name("update")
                .lastName("method")
                .idade(26)
                .email("emailatualizado@gmail.com").build();
        BDDMockito.when(repository.findById(originalUser.getId()))
                .thenReturn(Optional.of(originalUser));

        BDDMockito.doNothing().when(mapper).UserToUpdate(putRequest, originalUser);

        BDDMockito.when(repository.save(originalUser)).thenReturn(originalUser);

        BDDMockito.when(mapper.toUserPutResponse(originalUser)).thenReturn(putResponse);

        UserPutResponse resultado = service.update(originalUser.getId(), putRequest);

        Assertions.assertThat(resultado).isEqualTo(putResponse);
    }

}
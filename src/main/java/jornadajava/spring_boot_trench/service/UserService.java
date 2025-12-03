package jornadajava.spring_boot_trench.service;

import jornadajava.spring_boot_trench.domain.User;
import jornadajava.spring_boot_trench.exception.NotFoundException;
import jornadajava.spring_boot_trench.mapper.UserMapper;
import jornadajava.spring_boot_trench.repository.UserHardCodedRepository;
import jornadajava.spring_boot_trench.repository.UserRepository;
import jornadajava.spring_boot_trench.request.UserPostRequest;
import jornadajava.spring_boot_trench.request.UserPutRequest;
import jornadajava.spring_boot_trench.response.UserGetResponse;
import jornadajava.spring_boot_trench.response.UserPostResponse;
import jornadajava.spring_boot_trench.response.UserPutResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Log4j2
public class UserService {
    private final UserHardCodedRepository repository;
    private final UserRepository userRepository;
    private final UserMapper mapper;


    @Transactional(readOnly = true)
    public List<UserGetResponse> findAll(){
        return userRepository.findAll()
                .stream()
                .map(mapper::toUserGetResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public Page<UserGetResponse> findAllPaginated(Pageable pageable){
        var userPage = userRepository.findAll(pageable);

        return userPage.map(mapper::toUserGetResponse);
    }

    @Transactional(readOnly = true)
    public UserGetResponse findById(Long id){
        //faço um tratamento caso o id nao exista
        if (id == null){
            throw new NotFoundException("id cant be null or not exist");
        }
        //OptionalUser recebe o objeto encntrado pelo id
        return userRepository.findById(id).map(mapper::toUserGetResponse)
                .orElseThrow(()-> new NotFoundException("user not found"));
    }

    @Transactional(readOnly = true)
    public List<UserGetResponse> findByName(String name){
        //tratamento de exceção
        if (name.isEmpty()) {
            throw new NotFoundException("o parametro name esta vazio");
        }

        List<User> filteredList = userRepository.findByNameIgnoreCase(name);

        if (filteredList.isEmpty()) {
            throw new NotFoundException("o nome: " + name + " inserido não existe");
        }

        return filteredList
                .stream()
                .map(mapper::toUserGetResponse)
                .toList();
    }

    @Transactional
      //Executar esta anotação faz uma transação de banco de dados gerenciada pelo Spring.
      //Isso assegura a atomicidade (todas as operações de banco de dados neste método são concluídas com sucesso ou nenhuma delas é aplicada permanentemente)
      //e a consistência dos dados, fazendo rollback por padrão em caso de RuntimeExceptions.
    public void delete(Long id){
        if (id == null){
            throw new NotFoundException("id cant be null");
        }
        User userToDelete = userRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("id not found"));

        userRepository.delete(userToDelete);
        log.info("User deleted {}", userToDelete);
        return;
    }

    @Transactional
    public UserPutResponse update(Long id, UserPutRequest putRequest){
        if (id == null){
            throw new NotFoundException("id cant be null");
        }
        assertEmailDoesNotExist(putRequest.getEmail(), id);

        var userToUpdate = userRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("id not found"));


        //put request é oq vai ser usado para mudar no userToUpdate
        mapper.UserToUpdate(putRequest, userToUpdate);
        //e entao salvamos o userToUpdate atualizado no banco
        User updatedUser = userRepository.save(userToUpdate);
        log.info("User updated {}", updatedUser);

        return mapper.toUserPutResponse(updatedUser);
    }

    @Transactional
    public UserPostResponse saveUser(UserPostRequest postRequest){
        assertEmailDoesNotExist(postRequest.getEmail());

        //user recebe postRequest mapeado para dominio
        User user = mapper.toUser(postRequest);
        //setamos id para null
        user.setId(null);
        //salvamos o user no repoitorio
        User savedUser = userRepository.save(user);
        log.info("User created with id {}", savedUser.getId());
        //retornamos para o cliente oq foi salvo
        return mapper.toUserPostResponse(savedUser);
    }

    public void assertEmailDoesNotExist(String email){
        userRepository.findByEmail(email)
                .ifPresent(user -> {
                    throwEmailExistsException();
                });
    }

    public void assertEmailDoesNotExist(String email, Long id){
        userRepository.findByEmailAndIdNot(email, id)
                .ifPresent(user -> {throwEmailExistsException();});
    }

    private static void throwEmailExistsException() {
        throw new ResponseStatusException(HttpStatus.CONFLICT, "este email ja existe");
    }

}

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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
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


    public List<UserGetResponse> getAll(){
        return userRepository.findAll()
                .stream()
                .map(mapper::toUserGetResponse)
                .toList();
    }

    public UserGetResponse findById(Long id){
        //faço um tratamento caso o id nao exista
        if (id == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "id cant be null or not exist");
        }
        //OptionalUser recebe o objeto encntrado pelo id
        java.util.Optional<User> optionalUser = repository.findById(id);

        //aqui estou verificando se o objeto existe, se sim variavel user recebe optionalUser, se nao, exception é lançada
        User user = optionalUser
                .orElseThrow(() -> new NotFoundException("id not found"));
        log.info("Showing user found by id {}", user);

        //mapeando objeto para DTO e retornando para o cliente
        return mapper.toUserGetResponse(user);
    }

    public List<UserGetResponse> findByName(String name){
        //tratamento de exceção
        if (name.isEmpty()) {
            throw new NotFoundException("o parametro name esta vazio");
        }
        //aqui estamos criando a lista de nomes e chamando o metodo para adicionar todos os nomes requisitado pelo cliente(parametro name)
        List<User> filteredList = repository.findByName(name);

        if (filteredList.isEmpty()) {
            throw new NotFoundException("o nome: " + name + " inserido não existe");
        }

        //criando a lista para a adicionar os objetos DTOs
        List<UserGetResponse> getResponse = new ArrayList<>();
        //percorrendo toda a lista de nomes
        for (int i = 0; i < filteredList.size(); i++){
            //a cada volta no loop, le o nome da lista
            User user = filteredList.get(i);
            //transformando os objetos para DTO e adicionando na lista criada
            var dto = mapper.toUserGetResponse(user);
            log.info("showing list of names {}", dto);
            getResponse.add(dto);
        }
        //retornando a lista getResponse para o cliente
        return getResponse;
    }

    public void delete(Long id){
        if (id == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "id cant be null");
        }
        var userOptional = repository.findById(id);

        User userToDelete = userOptional
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "id not found"));

        repository.deleteById(userToDelete.getId());
        log.info("User deleted {}", userToDelete);
        return;
    }

    public UserPutResponse update(Long id, UserPutRequest putRequest){
        if (id == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "id cant be null");
        }

        var userOptional = repository.findById(id);
        User userToUpdate = userOptional
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "id nao eoncontrado"));

        mapper.UserToUpdate(putRequest, userToUpdate);
        User updatedUser = repository.update(userToUpdate);
        log.info("User updated {}", updatedUser);

        return mapper.toUserPutResponse(updatedUser);
    }

    public UserPostResponse saveUser(UserPostRequest postRequest){
        if (postRequest == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "you need to fill in all the atributtes");
        }

        //user recebe postRequest mapeado para dominio
        User user = mapper.toUser(postRequest);
        //setamos id para null
        user.setId(null);
        //salvamos o user no repoitorio
        User savedUser = repository.save(user);
        log.info("User created with id {}", savedUser.getId());
        //retornamos para o cliente oq foi salvo
        return mapper.toUserPostResponse(savedUser);
    }

}

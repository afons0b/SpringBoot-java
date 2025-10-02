package jornadajava.spring_boot_trench.service;

import jornadajava.spring_boot_trench.domain.Serie;
import jornadajava.spring_boot_trench.domain.User;
import jornadajava.spring_boot_trench.mapper.UserMapper;
import jornadajava.spring_boot_trench.repository.UserRepository;
import jornadajava.spring_boot_trench.response.SerieGetResponse;
import jornadajava.spring_boot_trench.response.UserGetResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository repository;
    private final UserMapper mapper;


    public List<UserGetResponse> getAll(){
        List<User> allList = repository.listAll();

        List<UserGetResponse> responseList = new ArrayList<>();
        for (int i = 0; i < repository.listAll().size(); i++){
            User user = repository.listAll().get(i);
            var dto = mapper.toUserGetResponse(user);

            responseList.add(dto);
        }
        return responseList;
    }

    public UserGetResponse findById(Long id){
        if (id == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "id não pode ser nulo");
        }
        java.util.Optional<User> optionalUser = repository.findById(id);

        User user = optionalUser
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Id nao encontrado"));

        return mapper.toUserGetResponse(user);
    }

    public List<UserGetResponse> findByName(String name){

        List<User> filteredList = repository.findByName(name);
        if (filteredList.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário com nome '" + name + "' não encontrado");
        }

        List<UserGetResponse> getResponse = new ArrayList<>();
        for (User user : filteredList){
            var dto = mapper.toUserGetResponse(user);
            getResponse.add(dto);
        }
        return getResponse;
    }

    public void delete(Long id){
        if (id == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "id nao pode ser nulo");
        }
        var userOptional = repository.findById(id);

        User userToDelete = userOptional
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "id nao encontrado"));

        repository.deleteById(userToDelete.getId());
    }


}

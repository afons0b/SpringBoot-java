package jornadajava.spring_boot_trench.repository;

import jornadajava.spring_boot_trench.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class UserRepository {
    //meu reposiorio precisa do UserData entao faremos uma injeção de dependencia
    private final UserData userData;
    //injeçao de dependencia feita, então posso partir para os metodos

    public List<User> listAll(){
        return userData.getUsers();
    }

    public List<User> findByName(String name){
        //este metodo retorna uma lista de usuarios com o mesmo nome,
        //entao criamos uma lista para isso
        List<User> filteredNameList = new ArrayList<>();

        //aqui percorremos toda a lista ate chegar ao tamanho maximo dela
        for (int i = 0; i < userData.getUsers().size(); i++){
            //e entao "registramos" cada usuario da lista
            User user = userData.getUsers().get(i);

            if (user.getName().equalsIgnoreCase(name)){
                filteredNameList.add(user);
            }
        }
        return filteredNameList;
    }

    //o metodo promete retornar um Optional, basicamente ele ja trata de dar uma resposta caso o id nao exista
    //o optional se aplica somente se for somente um objeto
    //eu poderia usar no findByName mas é um alista de nomes
    public Optional<User> findById(Long id){
        for (int i = 0; i < userData.getUsers().size(); i++){
            User user = userData.getUsers().get(i);

            if (user.getId().equals(id)){
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    public void deleteById(Long id){
        for (int i =0; i < userData.getUsers().size(); i++){
            User user = userData.getUsers().get(i);

            if (user.getId().equals(id)){
                userData.getUsers().remove(i);
                return;
            }
        }
    }

    public User save(User user){
        if (user.getId()==null){
            user.setId(userData.getNewId());
            userData.getUsers().add(user);
        }
        return user;
    }

    public User update(User user){
        deleteById(user.getId());
        userData.getUsers().add(user);
        return user;
    }
}

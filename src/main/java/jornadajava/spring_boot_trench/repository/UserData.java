package jornadajava.spring_boot_trench.repository;

import jornadajava.spring_boot_trench.domain.Serie;
import jornadajava.spring_boot_trench.domain.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserData {
    //criação da lista da classe User
    private final List<User> users = new ArrayList<>();
    private Long newId = 5L;

    public Long getNewId(){
        return newId++;
    }

    {
        var user1 = User.builder().id(1L).name("Afonso").idade(24).lastName("Braga").email("afonsobaraga@email.com").build();
        var user2 = User.builder().id(2L).name("Iuri").idade(23).lastName("Maciel").email("Iurimaciel@email.com").build();
        var user3 = User.builder().id(3L).name("Tales").idade(20).lastName("Ribeiro").email("talesribeiro@email.com").build();
        var user4 = User.builder().id(4L).name("Antonio").idade(38).lastName("Hidaka").email("antoniohidaka@email.com").build();
        users.addAll(List.of(user1, user2, user3, user4));
    }

    //aqui um metodo para retornar a lista users
    public List<User> getUsers(){
        return users;
    }
}

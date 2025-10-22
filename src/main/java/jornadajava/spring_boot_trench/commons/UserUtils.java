package jornadajava.spring_boot_trench.commons;

import jornadajava.spring_boot_trench.domain.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
public class UserUtils {

    public List<User> userList(){

        var user1 = User.builder().id(1L).name("Afonso").idade(24).lastName("Braga").email("afonsobaraga@email.com").build();
        var user2 = User.builder().id(2L).name("Iuri").idade(23).lastName("Maciel").email("Iurimaciel@email.com").build();
        var user3 = User.builder().id(3L).name("Tales").idade(20).lastName("Ribeiro").email("talesribeiro@email.com").build();
        var user4 = User.builder().id(4L).name("Antonio").idade(38).lastName("Hidaka").email("antoniohidaka@email.com").build();
        var user5 = User.builder().id(5L).name("Afonso").idade(24).lastName("Braga").email("afonsobaraga@email.com").build();

        return new ArrayList<>(List.of(user1, user2, user3, user4, user5));

    }

    public User userToSave(){
        return User.builder()
                .id(99L)
                .name("usuario")
                .lastName("teste")
                .idade(200)
                .email("usuarioteste@email.com").build();
    }
}

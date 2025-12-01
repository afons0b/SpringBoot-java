package jornadajava.spring_boot_trench.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User implements UserDetails {
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private int idade;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String roles;


     //LÓGICA DE PERMISSÕES:
     //O banco guarda as roles como uma única String separada por vírgulas.
     //O Spring Security precisa de uma Coleção de objetos do tipo 'GrantedAuthority'.
     //Este método faz a conversão.

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 1. Pegamos a string "ADMIN,USER" e cortamos onde tem vírgula (split).
        //    Isso cria um Array de strings ["ADMIN", "USER"].
        return Arrays.stream(roles.split(","))
                // 2. Para cada string (ex: "ADMIN"), criamos um objeto 'SimpleGrantedAuthority'.
                //    Esse é o objeto oficial que o Spring entende como uma "Permissão".
                .map(SimpleGrantedAuthority::new)
                // 3. Juntamos tudo numa lista e devolvemos pro Spring Security
                .toList();
    }


     //LÓGICA DE SENHA:
     //O Spring chama isso para pegar a senha DO BANCO.
     //Depois, ele pega a senha que o usuário digitou no login,
     //aplica o mesmo algoritmo de Hash, e compara com o valor retornado aqui.

    @Override
    public String getPassword() {
        return this.password;
    }


      //LÓGICA DE IDENTIFICAÇÃO (LOGIN):
    //O Spring chama isso pra saber "qual é o login desse cara?".
     //Como decidimos que o login é via EMAIL, retornamos o campo email aqui.
     //Se quiséssemos logar com CPF, retornaríamos o CPF aqui.

    @Override
    public String getUsername() {
        return this.email;
    }
}

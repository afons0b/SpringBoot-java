package jornadajava.spring_boot_trench.service;

import jornadajava.spring_boot_trench.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

//NOTA GERAL:
//Esta classe é o "Google Tradutor" entre o Spring Security e o nosso Repository.
//O Spring diz: "Preciso de um usuário".
//Esta classe diz: "Deixa comigo, vou buscar no MySQL".
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository repository;

     //LÓGICA DE BUSCA:
     //Este é o ÚNICO método que o Spring Security chama no momento do login.
     //Ele passa o que o usuário digitou no campo "login" (username).

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. O Spring nos dá o 'username' (o que foi digitado no login).
        //    Nós usamos nosso repository para buscar pelo EMAIL (pois definimos que email é o login).
        return repository.
                findByEmail(username)
                // 2. Se o banco retornar vazio (Optional.empty), significa que o email não existe.
                //    Nesse caso, SOMOS OBRIGADOS a lançar a exceção 'UsernameNotFoundException'.
                //    O Spring pega essa exceção e transforma num erro de "Login Falhou".
                .orElseThrow(() -> new UsernameNotFoundException("User not found for email " + username));
    }
}



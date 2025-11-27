package jornadajava.spring_boot_trench.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    //aqui estamos montando uma lista de coisas que o spring secutiry nao precisara cobrar autenticação
    //e estamos dando total permissao para o swagger, pois a documentação da API nao precisa de segurança
    private static final String[] WHITE_LIST = {"/swagger-ui.html", "/v3/api-docs/**", "/swagger-ui/**"};

    //aqui estou fazendo usuarios que podem ter permissao para acessar os endpoints da API
    //e tbm estou encriptando cada senha para segurança
    @Bean
    public UserDetailsService userDetailsService() {
        log.info(passwordEncoder().encode("jokinha"));
        var user = User.withUsername("joca")
                .password(passwordEncoder().encode("jokinha"))
                .roles("USER")
                .build();

        var admin = User.withUsername("ADMIN")
                .password(passwordEncoder().encode("system"))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user, admin);
    }

    //aqui esta sendo dada a autorização para a lista que foi criada acima
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        return http.authorizeHttpRequests
                (auth -> auth.requestMatchers(WHITE_LIST)
                        //lista sendo autorizada cm permitAll
                        .permitAll()
                        //porem para cada outra requisição, solicite autorização
                        .anyRequest()
                        //este httpBasic nada mais é que aquele pop up do navegador que solicita login e senha
                        .authenticated()).httpBasic(Customizer.withDefaults()).build();
    }

    @Bean
    //o passEncoder nada mais é que a criptografia da senha, ele usa o bcrypt e chamamos essa função para criptografia
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}

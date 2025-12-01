package jornadajava.spring_boot_trench.config;

import io.swagger.v3.oas.models.PathItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    //aqui estamos montando uma lista de coisas que o spring secutiry nao precisara cobrar autenticação
    //e estamos dando total permissao para o swagger, pois a documentação da API nao precisa de segurança
    private static final String[] WHITE_LIST = {"/swagger-ui.html", "/v3/api-docs/**", "/swagger-ui/**", "/csrf"};

    //aqui estou fazendo usuarios que podem ter permissao para acessar os endpoints da API
    //e tbm estou encriptando cada senha para segurança
    @Bean
    public UserDetailsService userDetailsService() {
        log.info(passwordEncoder().encode("jokinha"));
        var user = User.withUsername("joca")
                .password(passwordEncoder().encode("jokinha"))
                .roles("USER")
                .build();

        var admin = User.withUsername("afonso")
                .password(passwordEncoder().encode("system"))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user, admin);
    }

    //aqui esta sendo dada a autorização para a lista que foi criada acima
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        return http
                //Essa linha diz: "Não guarde o token no servidor. Guarde ele no navegador do usuário, dentro de um Cookie chamado XSRF-TOKEN
                .csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        //No Spring Security 6, houve uma mudança interna de performance que fazia o token não ser gerado/salvo em alguns momentos específicos se a gente não pedisse explicitamente.
                        //Esse RequestHandler é o "Gerente Proativo". Ele garante que o token seja gerado e disponibilizado como um atributo da requisição
                        //permitindo que o CookieCsrfTokenRepository (a linha de cima) consiga pegar esse token
                        //e escrever no cookie corretamente.
                        .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler()))
                .authorizeHttpRequests
                (auth -> auth.requestMatchers(WHITE_LIST)
                        //lista sendo autorizada cm permitAll
                        .permitAll()
                        .requestMatchers(HttpMethod.GET,"/v1/user").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/v1/user").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/v1/user/*").hasRole("ADMIN")
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

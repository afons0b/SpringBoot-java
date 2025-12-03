package jornadajava.spring_boot_trench.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Lista branca (Documentação e Endpoint de CSRF)
    private static final String[] WHITE_LIST = {
            "/swagger-ui.html",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/csrf" // O endpoint público pra pegar o token
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // 1. Configuração do CSRF (Proteção para Front-end)
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()) // Salva no Cookie legível por JS
                        .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())    // Garante o fluxo correto
                )
                // 2. As Regras de Autorização (Quem entra onde)
                .authorizeHttpRequests(auth -> auth
                        // Libera a documentação e o token CSRF para todos
                        .requestMatchers(WHITE_LIST).permitAll()

                        // PERMITE CRIAR USUÁRIO (Sign-up tem que ser público!)
                        .requestMatchers(HttpMethod.POST, "/v1/user").permitAll()

                        // SÓ ADMIN VÊ A LISTA DE USUÁRIOS
                        // OBS: O Spring espera que no banco a role esteja salva como "ROLE_ADMIN"
                        // ou que você use hasAuthority("ADMIN") se salvou sem o prefixo.
                        .requestMatchers(HttpMethod.GET, "/v1/user").hasAuthority("ADMIN")

                        // SÓ ADMIN DELETA E FILTRA
                        .requestMatchers(HttpMethod.DELETE, "/v1/user/*").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/v1/user/findByName").hasAuthority("USER")

                        // Qualquer outra coisa: TEM QUE ESTAR LOGADO
                        .anyRequest().authenticated()
                )
                // 3. Login Padrão (Popup do navegador ou Header Authorization)
                .httpBasic(Customizer.withDefaults())
                .build();
    }
}
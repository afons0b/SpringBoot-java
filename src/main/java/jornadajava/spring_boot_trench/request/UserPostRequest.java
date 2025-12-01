package jornadajava.spring_boot_trench.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserPostRequest {
    @NotBlank(message = "campo nome é necessario")
    private String name;
    private int idade;
    @NotBlank(message = "o campo lastName é necessario") //@NotBlank essa anotação serve para que o campo lastName nao seja nulo(null), nem em branco(" ") e nem vazio("")
    private String lastName;
    @NotBlank(message = "campo email é necessario")
    //anotação @Email valida o campo email e verificar se o email atende a expressão valida
    @Email(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,8}$",message = "o email inserido é invalido")
    private String email;
    @NotBlank(message = "campo password é necessario")
    private String password;

}

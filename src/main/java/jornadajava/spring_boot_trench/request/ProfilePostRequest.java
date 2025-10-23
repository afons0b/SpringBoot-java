package jornadajava.spring_boot_trench.request;

import jakarta.validation.constraints.NotBlank;

public class ProfilePostRequest {
    @NotBlank(message = "campo nome é necessario")
    private String name;
    @NotBlank(message = "o campo lastName é necessario") //@NotBlank essa anotação serve para que o campo lastName nao seja nulo(null), nem em branco(" ") e nem vazio("")
    private String description;

}

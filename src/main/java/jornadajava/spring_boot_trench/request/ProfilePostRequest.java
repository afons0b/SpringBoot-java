package jornadajava.spring_boot_trench.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProfilePostRequest {
    @NotBlank(message = "campo nome é necessario")
    private String name;
    @NotBlank(message = "o campo descrição é necessario")
    private String description;

}

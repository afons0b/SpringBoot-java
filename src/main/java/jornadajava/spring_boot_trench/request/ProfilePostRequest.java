package jornadajava.spring_boot_trench.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfilePostRequest {
    @NotBlank(message = "campo nome é necessario")
    private String name;
    @NotBlank(message = "o campo descrição é necessario")
    private String description;

}

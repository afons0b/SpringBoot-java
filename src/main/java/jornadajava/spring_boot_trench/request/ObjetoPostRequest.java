package jornadajava.spring_boot_trench.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
public class ObjetoPostRequest {
    private Long ID;
    private String atributo1;

}

package jornadajava.spring_boot_trench.response;

import jornadajava.spring_boot_trench.domain.Objeto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
public class ObjetoGetResponse {
    private Long ID;
    private String atributo1;
    private LocalDateTime createdAt;
}

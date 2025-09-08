package jornadajava.spring_boot_trench.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SeriePutRequest {

    private Long id;
    private String nome;
    private int temporada;
}

package jornadajava.spring_boot_trench.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class SerieGetResponse {
    private Long id;
    private String nome;
    private int temporada;
    private LocalDateTime createdAt;
}

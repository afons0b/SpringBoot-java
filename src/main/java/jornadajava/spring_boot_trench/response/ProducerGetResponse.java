package jornadajava.spring_boot_trench.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@Builder
public class ProducerGetResponse {
    private Long id;
    private String nome;
    private LocalDateTime createdAt;
}

package jornadajava.spring_boot_trench.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Serie {

    @EqualsAndHashCode.Include
    private Long id;
    @JsonProperty("full_name")
    private String nome;
    private int temporada;
    private LocalDateTime createdAt;

}

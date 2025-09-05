package jornadajava.spring_boot_trench.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Filme {

    @EqualsAndHashCode.Include
    private Long id;
    @JsonProperty("name")
    private String nome;
    private String diretor;
    private int anoLancamento;
    private static List<Filme> filmes = new ArrayList<>();

    static {
        var paradoxo = Filme.builder().id(1L).nome("Paradoxo").diretor("John kennedy").anoLancamento(2000).build();
        var dinossauro = Filme.builder().id(2L).nome("Dinossauro").diretor("Samuel menegati").anoLancamento(2010).build();
        var rock = Filme.builder().id(3L).nome("Rock Balboa").diretor("Silver Stalone").anoLancamento(1990).build();
        filmes.addAll(List.of(paradoxo, dinossauro, rock));

    }

    public static List<Filme> getFilmes() {
        return filmes;
    }
}

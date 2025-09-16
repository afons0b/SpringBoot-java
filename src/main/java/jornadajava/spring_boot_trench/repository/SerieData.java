package jornadajava.spring_boot_trench.repository;

import jornadajava.spring_boot_trench.domain.Serie;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class SerieData {
    private final List<Serie> series = new ArrayList<>();
    private Long proximoID = 4L;

    public Long gerarId(){
        return proximoID++;
    }

     {
        var dexter = Serie.builder().id((1L)).nome("Dexter").temporada(1).createdAt(LocalDateTime.now()).build();
        var duna = Serie.builder().id(2L).nome("Duna").temporada(2).createdAt(LocalDateTime.now()).build();
        var dark = Serie.builder().id(3L).nome("Dark").temporada(3).createdAt(LocalDateTime.now()).build();
        series.addAll(List.of(dexter, duna, dark));
    }

    public List<Serie> getSeries() {
        return series;
    }
}

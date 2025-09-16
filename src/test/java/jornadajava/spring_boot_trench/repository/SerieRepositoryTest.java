package jornadajava.spring_boot_trench.repository;

import jornadajava.spring_boot_trench.domain.Serie;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)

class SerieRepositoryTest {

    @Mock
    private SerieData serieData;
    private final List<Serie> series = new ArrayList<>();

    @InjectMocks
    private SerieRepository repository;

    @BeforeEach
    void init(){
        var attackOntitan = Serie.builder().id((1L)).nome("Attack On Titan").temporada(1).createdAt(LocalDateTime.now()).build();
        var duna = Serie.builder().id(2L).nome("Duna").temporada(2).createdAt(LocalDateTime.now()).build();
        var dark = Serie.builder().id(3L).nome("Dark").temporada(3).createdAt(LocalDateTime.now()).build();
        series.addAll(List.of(attackOntitan, duna, dark));
    }


    @Test
    @DisplayName("findAll deveria retornar uma lista de series")
    void findAll_returnAllSeries_WhenSuccessful(){
        BDDMockito.when(serieData.getSeries()).thenReturn(series);

        var series = repository.findAll();
        Assertions.assertThat(series).isNotNull().hasSize(series.size());
    }
    

}
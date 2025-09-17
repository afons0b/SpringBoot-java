package jornadajava.spring_boot_trench.service;

import jornadajava.spring_boot_trench.domain.Serie;
import jornadajava.spring_boot_trench.repository.SerieRepository;
import jornadajava.spring_boot_trench.response.SerieGetResponse;
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
class SerieServiceTest {

    @Mock
    private SerieRepository repository;
    private final List<Serie> series = new ArrayList<>();

    @InjectMocks
    private SerieService service;

    @BeforeEach
    void init(){

        var attackOntitan = Serie.builder().id((1L)).nome("Attack On Titan").temporada(1).createdAt(LocalDateTime.now()).build();
        var duna = Serie.builder().id(2L).nome("Duna").temporada(2).createdAt(LocalDateTime.now()).build();
        var dark = Serie.builder().id(3L).nome("Dark").temporada(3).createdAt(LocalDateTime.now()).build();
        series.addAll(List.of(attackOntitan, duna, dark));
    }

    @Test
    @DisplayName("teste para receber a serie com mais temporadas")
    void find_returnTVserie_withMore_season(){
        BDDMockito.when(repository.findAll()).thenReturn(series);

        SerieGetResponse serieLonga = service.findLongestTVSerie();

        org.assertj.core.api.Assertions.assertThat(serieLonga).isNotNull();

        Assertions.assertThat(serieLonga.getNome()).isEqualTo("Dark");

    }
}
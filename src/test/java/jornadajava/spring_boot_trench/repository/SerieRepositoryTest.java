package jornadajava.spring_boot_trench.repository;

import jornadajava.spring_boot_trench.domain.Serie;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.class)

class SerieRepositoryTest {

    @Mock
    private SerieData serieData;
    private final List<Serie> serieList = new ArrayList<>();

    @InjectMocks
    private SerieRepository repository;

    @BeforeEach
    void init(){
        var attackOntitan = Serie.builder().id((1L)).nome("Attack On Titan").temporada(1).createdAt(LocalDateTime.now()).build();
        var duna = Serie.builder().id(2L).nome("Duna").temporada(2).createdAt(LocalDateTime.now()).build();
        var dark = Serie.builder().id(3L).nome("Dark").temporada(3).createdAt(LocalDateTime.now()).build();
        serieList.addAll(List.of(attackOntitan, duna, dark));
    }


    @Test
    @DisplayName("findAll deveria retornar uma lista de series")
    @Order(1)
    void findAll_returnAllSeries_WhenSuccessful(){
        BDDMockito.when(serieData.getSeries()).thenReturn(serieList);

        var series = repository.findAll();
        Assertions.assertThat(series).isNotNull().hasSize(series.size());
    }

    @Test
    @DisplayName("findById deveria retornar uma serie pelo Id")
    @Order(2)
    void findById_returningId(){
        BDDMockito.when(serieData.getSeries()).thenReturn(serieList);

        var expectedSerie = serieList.getFirst();

        var series = repository.findById(expectedSerie.getId());

        Assertions.assertThat(series).isNotNull();

        Assertions.assertThat(series)
                .isPresent()
                .get()
                .isEqualTo(expectedSerie);

    }

    @Test
    @DisplayName("findByName deveria retornar uma lista de nomes de series")
    @Order(3)
    void findByName_returning_list_of_names(){
        BDDMockito.when(serieData.getSeries()).thenReturn(serieList);

        var expectedSerie = serieList.getFirst();

        var seriesList = repository.findByName(expectedSerie.getNome());

        Assertions.assertThat(seriesList)
                .isNotEmpty()
                .contains(expectedSerie);
    }

    @Test
    @DisplayName("save deveria criar e salvar um novo objeto")
    @Order(4)
    void saveMethod_shouldCreate_andSaved_newObject(){
        BDDMockito.when(serieData.getSeries()).thenReturn(serieList);
        BDDMockito.when(serieData.gerarId()).thenReturn(4L);

        System.out.println("Tamanho da lista: " + serieList.size());

        var novaSerie = Serie.builder()
                .id(null)
                .nome("Smiling Friends")
                .temporada(2)
                .createdAt(LocalDateTime.now())
                .build();

        var serieSalva = repository.save(novaSerie);

        System.out.println("ID gerado: " + serieSalva.getId());

        Assertions.assertThat(serieSalva.getId()).isNotNull();

        Assertions.assertThat(serieSalva.getId()).isEqualTo(4L);

        Assertions.assertThat(repository.findAll()).contains(serieSalva);
    }

    @Test
    @DisplayName("metodo delete remove uma serie")
    @Order(5)
    void delete_method_FindById(){
        BDDMockito.when(serieData.getSeries()).thenReturn(serieList);

        var serieRemovida = serieList.getFirst();
        repository.delete(serieRemovida);

        Assertions.assertThat(serieList).isNotEmpty().doesNotContain(serieRemovida);

    }
}
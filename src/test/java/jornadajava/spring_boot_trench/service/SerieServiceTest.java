package jornadajava.spring_boot_trench.service;

import jornadajava.spring_boot_trench.domain.Serie;
import jornadajava.spring_boot_trench.mapper.SerieMapper;
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
    private final List<Serie> serieListDomain = new ArrayList<>();
    private final List<SerieGetResponse> serieListResponse = new ArrayList<>();

    @Mock
    private SerieMapper mapper;

    @InjectMocks
    private SerieService service;

    @BeforeEach
    void init(){

        LocalDateTime tempo = LocalDateTime.now();

        var attackOntitan = Serie.builder().id((1L)).nome("Attack On Titan").temporada(1).createdAt(tempo).build();
        var duna = Serie.builder().id(2L).nome("Duna").temporada(2).createdAt(tempo).build();
        var dark = Serie.builder().id(3L).nome("Dark").temporada(3).createdAt(tempo).build();
        serieListDomain.addAll(List.of(attackOntitan, duna, dark));

        var attackOntitanResponse = SerieGetResponse.builder().id((1L)).nome("Attack On Titan").temporada(1).createdAt(tempo).build();
        var dunaResponse = SerieGetResponse.builder().id(2L).nome("Duna").temporada(2).createdAt(tempo).build();
        var darkResponse = SerieGetResponse.builder().id(3L).nome("Dark").temporada(3).createdAt(tempo).build();
        serieListResponse.addAll(List.of(attackOntitanResponse, dunaResponse, darkResponse));
    }

    @Test
    @DisplayName("teste para receber a serie com mais temporadas")
    void find_returnTVserie_withMore_season(){
        BDDMockito.when(repository.findAll()).thenReturn(serieListDomain);

        SerieGetResponse serieLonga = service.findLongestTVSerie();

        System.out.println("Nome gerado: " + serieLonga.getNome());

        org.assertj.core.api.Assertions.assertThat(serieLonga).isNotNull();

        Assertions.assertThat(serieLonga.getNome()).isEqualTo("Dark");

    }

    @Test
    @DisplayName("teste para retornar todos os objetos")
    void findAll_method() {
        BDDMockito.when(mapper.toSerieGetResponseList(serieListDomain)).thenReturn(serieListResponse);

        List<SerieGetResponse> responses = service.getAll();

        Assertions.assertThat(responses).usingRecursiveComparison().ignoringFields("createdAt").isEqualTo(serieListResponse);
    }

}
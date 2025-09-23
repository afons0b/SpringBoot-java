package jornadajava.spring_boot_trench.service;

import jornadajava.spring_boot_trench.domain.Serie;
import jornadajava.spring_boot_trench.mapper.SerieMapper;
import jornadajava.spring_boot_trench.repository.SerieRepository;
import jornadajava.spring_boot_trench.request.SeriePostRequest;
import jornadajava.spring_boot_trench.request.SeriePutRequest;
import jornadajava.spring_boot_trench.response.SerieGetResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    @Order(1)
    void find_returnTVserie_withMore_season(){
        BDDMockito.when(repository.findAll()).thenReturn(serieListDomain);

        SerieGetResponse serieLonga = service.findLongestTVSerie();

        System.out.println("Nome gerado: " + serieLonga.getNome());

        org.assertj.core.api.Assertions.assertThat(serieLonga).isNotNull();

        Assertions.assertThat(serieLonga.getNome()).isEqualTo("Dark");

    }

    @Test
    @DisplayName("getAll (com for-loop no service) deve retornar lista de DTOs")
    @Order(2)
    void getAll_comForLoop_retornaListaDeDtos() {

        BDDMockito.when(repository.findAll()).thenReturn(serieListDomain);

        for (int i = 0; i < serieListDomain.size(); i++) {
            Serie serieDomain = serieListDomain.get(i);
            SerieGetResponse serieGetResponse = serieListResponse.get(i);

            BDDMockito.when(mapper.toSerieGetResponse(serieDomain)).thenReturn(serieGetResponse);
        }

        List<SerieGetResponse> resultado = service.getAll();

        Assertions.assertThat(resultado).isNotNull().isEqualTo(serieListResponse);

        Assertions.assertThat(serieListResponse).hasSize(3);
    }

    @Test
    @DisplayName("metodo findByName deve retornar uma lista de nomes usando como referencia o parametro passado")
    @Order(3)
    void findByName_deveRetornar_umaLista(){

        List<Serie> dunaSerie = List.of(serieListDomain.get(1), serieListDomain.get(1));
        List<SerieGetResponse> dunaResponse = List.of(serieListResponse.get(1), serieListResponse.get(1));

        BDDMockito.when(repository.findByName("Duna")).thenReturn(dunaSerie);

        for (int i = 0; i < dunaSerie.size(); i++){
            BDDMockito.when(mapper.toSerieGetResponse(dunaSerie.get(i)))
                    .thenReturn(dunaResponse.get(i));
        }

        List<SerieGetResponse> responses = service.findByName("Duna");

        Assertions.assertThat(responses).hasSize(2);

        Assertions.assertThat(responses).isEqualTo(dunaResponse);

        System.out.println("Nome gerado: " + responses);

    }

    @Test
    @DisplayName("metodo para buscar um objeto pelo id")
    @Order(4)
    void findById_deveRetornar_umObjeto_peloId(){

        Serie serie = serieListDomain.getFirst();
        SerieGetResponse serieResponse = serieListResponse.getFirst();

        BDDMockito.when(repository.findById(serie.getId())).thenReturn(Optional.of(serie));
        BDDMockito.when(mapper.toSerieGetResponse(serie)).thenReturn(serieResponse);

        SerieGetResponse response = service.findById(serie.getId());

        Assertions.assertThat(response).isEqualTo(serieResponse);
    }

    @Test
    @DisplayName("metodo para deletar um objeto pelo id")
    @Order(5)
    void deleteById_testMethod(){
        //pegando o primeiro objeo da lista
        Serie serieToDelete = serieListDomain.getFirst();

        //afirmamos que a lista possui 3 objetos
        Assertions.assertThat(serieListDomain).hasSize(3);

        //ensinando que: quando chamamos o findById do repositorio, retornamos um Optional da variavel serieToDelete
        BDDMockito.when(repository.findById(serieToDelete.getId()))
                .thenReturn(Optional.of(serieToDelete));

        service.delete(serieToDelete.getId());

        BDDMockito.verify(repository, BDDMockito.times(1)).delete(serieToDelete);
    }

    @Test
    @DisplayName("metodo para salvar um objeto na lista")
    @Order(6)
    void save_method_test(){

        //chegada do dto postRequest
        SeriePostRequest postRequest = SeriePostRequest.builder()
                .nome("Breaking Bad").temporada(5)
                .build();

        //aqui simula a logica do repositorio
        Serie serieDomain = Serie.builder()
                .id(null).nome("Breaking Bad").temporada(5)
                .build();

        //serie salva no banco
        Serie serieSalva = Serie.builder()
                .id(4L).nome("Breaking Bad").temporada(5).createdAt(LocalDateTime.now())
                .build();

        //retorno da serie como dto SerieGetResponse
        SerieGetResponse expectedResponse = SerieGetResponse.builder()
                .id(4L).nome("Breaking Bad").temporada(5).createdAt(LocalDateTime.now())
                .build();

        //cliente envia o objeto como dto postRequest
        BDDMockito.when(mapper.toSerie(postRequest)).thenReturn(serieDomain);
        //repositorio salva no banco como dominio
        BDDMockito.when(repository.save(serieDomain)).thenReturn(serieSalva);
        //e o mapper retorna o objeto salvo como dto para o cliente
        BDDMockito.when(mapper.toSerieGetResponse(serieSalva)).thenReturn(expectedResponse);

        SerieGetResponse response = service.save(postRequest);

        Assertions.assertThat(response).isEqualTo(expectedResponse);
        Assertions.assertThat(response.getId()).isNotNull();

        BDDMockito.verify(repository, BDDMockito
                .times(1)).save(serieDomain);

        List<Serie> listaatualizada = repository.findAll();
        Assertions.assertThat(listaatualizada).hasSize(4);
        Assertions.assertThat(listaatualizada).contains(serieSalva);

    }

    @Test
    @DisplayName("metodo para atualizar um objeto usando DTO")
    @Order(7)
    void update_method(){
        //chegada do putRequest
        SeriePutRequest putRequest = SeriePutRequest.builder()
                .id(1L)
                .nome("Kaijuno8")
                .temporada(3)
                .build();

        Serie serieToUpdate = serieListDomain.getFirst();

        SerieGetResponse response = SerieGetResponse.builder()
                .id(serieToUpdate.getId())
                .nome(putRequest.getNome())
                .temporada(putRequest.getTemporada())
                .build();

        BDDMockito.when(repository.findById(serieToUpdate.getId()))
                .thenReturn(Optional.of(serieToUpdate));

        BDDMockito.doNothing().when(mapper).SerieToUpdate(putRequest, serieToUpdate);

        BDDMockito.when(repository.save(serieToUpdate)).thenReturn(serieToUpdate);

        BDDMockito.when(mapper.toSerieGetResponse(serieToUpdate)).thenReturn(response);

        SerieGetResponse updateResponse = service.update(serieToUpdate.getId(), putRequest);

        Assertions.assertThat(updateResponse).isEqualTo(response);

    }
}
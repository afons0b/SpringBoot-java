package jornadajava.spring_boot_trench.controllers;


import jornadajava.spring_boot_trench.domain.Serie;
import jornadajava.spring_boot_trench.mapper.SerieMapperImpl;
import jornadajava.spring_boot_trench.repository.SerieData;
import jornadajava.spring_boot_trench.repository.SerieRepository;
import jornadajava.spring_boot_trench.service.SerieService;
import org.junit.jupiter.api.*;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;
import java.nio.file.Files;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@WebMvcTest(controllers = SerieController.class)
@Import({SerieMapperImpl.class, SerieService.class, SerieRepository.class, SerieData.class})
class SerieControllerTest {
    @Autowired
    //o MockMvc é como se fosse um postman mas emulado no teste, um postman de mentira
    private MockMvc mockMvc;

    @MockitoBean
    private SerieData serieData;
    private List<Serie> serieList = new ArrayList<>();
    @Autowired
    private ResourceLoader resourceLoader;

    @BeforeEach
    void init(){
        var dateTime = "2025-09-25T15:25:06.034355971";
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS");
        var LocalDateTime = java.time.LocalDateTime.parse(dateTime, formatter);

        var attackOntitan = Serie.builder().id((1L)).nome("Kaijuno8").temporada(1).createdAt(LocalDateTime).build();
        var duna = Serie.builder().id(2L).nome("Duna").temporada(2).createdAt(LocalDateTime).build();
        var dark = Serie.builder().id(3L).nome("Dark").temporada(3).createdAt(LocalDateTime).build();
        serieList.addAll(List.of(attackOntitan, duna, dark));
    }

    @Test
    @DisplayName("getAll deve retornar lista de DTOs")
    @Order(1)
    void getAll_retornaListaDeDtos() throws Exception {
        BDDMockito.when(serieData.getSeries()).thenReturn(serieList);
        var response = readResourceLoader("serie/get-full-list.200.json");

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/serie"))
                //andDo e andExpect sao basicamente assertions,
                // esse primeiro diz para printar tudo no terminal
                .andDo(MockMvcResultHandlers.print())
                //o segundo espera que o status seja 200 ok
                .andExpect(MockMvcResultMatchers.status().isOk())
                //pra uma explicaçao melhor, o teste emula um get all,
                // ou seja alguem esta dando um get all e vai receber um json completo,
                //entao "esse assertion" compara o json emulado com o json do arquivo response
                //e verifica se ambos sao iguais, se for o teste passa
                .andExpect(MockMvcResultMatchers.content().json(response));

    }

    private String readResourceLoader(String fileName) throws IOException {
        //aqui estamos buscando o arquivo com o json para comparação,
        //o resourceloader usa o classpath para buscar o arquivo passado.
        // Resource loade esta dando o caminho pelo classpath e o get file ta encontrando ele
        var file = resourceLoader.getResource("classpath:%s".formatted(fileName)).getFile();
        //o readAllBytes le o json em dados brutos, literalmente bytes e dps é retornado como string para ser lido
        return new String(Files.readAllBytes(file.toPath()));
    }

    @Test
    @DisplayName("GET V1/serie?nome=Dark retorna uma lista de objetos com esse nome")
    @Order(2)
    void get_name_filtered() throws Exception{
        BDDMockito.when(serieData.getSeries()).thenReturn(serieList);
        var response = readResourceLoader("serie/get-name-list.200.json");
        var name = "Kaijuno8";

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/serie/filter").param("name", name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/serie/1 retorna uma serie com id passado")
    @Order(3)
    void findById_ReturnSeriePorId_quandoSucesso() throws Exception {
        BDDMockito.when(serieData.getSeries()).thenReturn(serieList);
        var id = 1L;
        var response = readResourceLoader("serie/get-serie-by-id.json");

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/serie/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/serie/1 retorna uma serie com id passado")
    @Order(4)
    void save_method() throws Exception {
        BDDMockito.when(serieData.gerarId()).thenReturn(4L);

        var request = readResourceLoader("serie/save-request-serie.201.json");

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/serie")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(4L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nome").value("Kaijuno8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.createdAt").exists());

    }

    @Test
    @DisplayName("metodo para testar atualizar um objeto usando DTO")
    @Order(7)
    void update_method() throws Exception {
        BDDMockito.when(serieData.getSeries()).thenReturn(serieList);
        var request = readResourceLoader("serie/put-request-serie-201.json");
        var id = 1L;

        mockMvc.perform(MockMvcRequestBuilders.put("/v1/serie/{id}", id)
                .content(request)
                .contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())

                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nome").value("Kaijuno"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.createdAt").exists());

    }

    @Test
    @DisplayName("metodo para deletar um objeto pelo id")
    @Order(5)
    void deleteById_testMethod() throws Exception {
        BDDMockito.when(serieData.getSeries()).thenReturn(serieList);

        var id = serieList.getFirst().getId();

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/serie/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
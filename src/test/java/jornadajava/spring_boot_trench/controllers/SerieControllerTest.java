package jornadajava.spring_boot_trench.controllers;


import jornadajava.spring_boot_trench.domain.Serie;
import jornadajava.spring_boot_trench.mapper.SerieMapperImpl;
import jornadajava.spring_boot_trench.repository.SerieData;
import jornadajava.spring_boot_trench.repository.SerieRepository;
import jornadajava.spring_boot_trench.service.SerieService;
import org.junit.jupiter.api.*;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@WebMvcTest(controllers = SerieController.class)
@Import({SerieMapperImpl.class, SerieService.class, SerieRepository.class, SerieData.class})
class SerieControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SerieData serieData;
    private List<Serie> serieList = new ArrayList<>();

    @BeforeEach
    void init(){
        var attackOntitan = Serie.builder().id((1L)).nome("Kaijuno8").temporada(1).createdAt(LocalDateTime.now()).build();
        var duna = Serie.builder().id(2L).nome("Duna").temporada(2).createdAt(LocalDateTime.now()).build();
        var dark = Serie.builder().id(3L).nome("Dark").temporada(3).createdAt(LocalDateTime.now()).build();
        serieList.addAll(List.of(attackOntitan, duna, dark));
    }

    @Test
    @DisplayName("getAll deve retornar lista de DTOs")
    @Order(1)
    void getAll_comForLoop_retornaListaDeDtos() throws Exception {
        BDDMockito.when(serieData.getSeries()).thenReturn(serieList);

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/serie"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].id")
                        .value(1L));

    }

}
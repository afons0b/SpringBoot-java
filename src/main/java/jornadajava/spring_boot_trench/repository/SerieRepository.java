package jornadajava.spring_boot_trench.repository;

import jornadajava.spring_boot_trench.domain.Serie;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class SerieRepository {


    private static final List<Serie> SERIES = new ArrayList<>();
    private static Long proximoID;

    static {
        var dexter = Serie.builder().id((1L)).nome("Dexter").temporada(1).createdAt(LocalDateTime.now()).build();
        var duna = Serie.builder().id(2L).nome("Duna").temporada(2).createdAt(LocalDateTime.now()).build();
        var dark = Serie.builder().id(3L).nome("Dark").temporada(3).createdAt(LocalDateTime.now()).build();
        SERIES.addAll(List.of(dexter, duna, dark));
        proximoID = (long) SERIES.size() + 1;
    }

    public List<Serie> findAll(){
        return SERIES;
    }

    public Optional<Serie> findById(Long id){

        for (Serie serie : findAll()){
            if (serie.getId().equals(id)){
                return Optional.of(serie);
            }
        }
        return Optional.empty();
    }

    public List<Serie> findByName(String name){
        List<Serie> filterList = new ArrayList<>();
        for (Serie serie : SERIES){
            if (serie.getNome().equalsIgnoreCase(name)){
                filterList.add(serie);
            }
        }
        return filterList;
    }

    public void delete(Serie serie){
        for ( int i = 0; i < SERIES.size(); i++ ){
            if (SERIES.get(i).getId().equals(serie.getId())){
                SERIES.remove(i);
                return;
            }
        }
    }


    public Serie save(Serie serie){
        if (serie.getId()==null){
            serie.setCreatedAt(LocalDateTime.now());
            serie.setId(proximoID++);
            SERIES.add(serie);
        }else {
            SERIES.removeIf(serieExistente -> serieExistente.getId().equals(serie.getId()));
            SERIES.add(serie);
        }
        return serie;
    }


}

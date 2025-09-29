package jornadajava.spring_boot_trench.repository;

import external.dependency.Connection;
import jornadajava.spring_boot_trench.domain.Serie;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Log4j2
public class SerieRepository {
    private final Connection connection;
    private final SerieData serieData;

    public List<Serie> findAll(){
        return serieData.getSeries();
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
        for (Serie serie : serieData.getSeries()){
            if (serie.getNome().equalsIgnoreCase(name)){
                filterList.add(serie);
            }
        }
        return filterList;
    }

    public void delete(Serie serie){
        for ( int i = 0; i < serieData.getSeries().size(); i++ ){
            if (serieData.getSeries().get(i).getId().equals(serie.getId())){
                serieData.getSeries().remove(i);
                return;
            }
        }
    }


    public Serie save(Serie serie){
        if (serie.getId()==null){
            serie.setCreatedAt(LocalDateTime.now());
            serie.setId(serieData.gerarId());
            serieData.getSeries().add(serie);
        }else {
            serieData.getSeries().removeIf(serieExistente -> serieExistente.getId().equals(serie.getId()));
            serieData.getSeries().add(serie);
        }
        return serie;
    }


}

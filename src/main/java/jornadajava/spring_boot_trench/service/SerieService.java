package jornadajava.spring_boot_trench.service;

import jornadajava.spring_boot_trench.domain.Serie;
import jornadajava.spring_boot_trench.mapper.SerieMapper;
import jornadajava.spring_boot_trench.repository.SerieRepository;
import jornadajava.spring_boot_trench.response.SerieGetResponse;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Service
public class SerieService {

    private static final SerieMapper mapper = SerieMapper.MAPPER;

    private SerieRepository repository;

    public SerieService(){
        this.repository = new SerieRepository();
    }

    public List<SerieGetResponse> findByName(String name){

        List<Serie> filteredList = repository.findByName(name);
        List<SerieGetResponse> getResponse = new ArrayList<>();
        for (Serie serie : filteredList){
            var dto = mapper.toSerieGetResponse(serie);
            getResponse.add(dto);
        }
        return getResponse;
    }
}

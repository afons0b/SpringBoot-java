package jornadajava.spring_boot_trench.service;

import jornadajava.spring_boot_trench.domain.Serie;
import jornadajava.spring_boot_trench.mapper.SerieMapper;
import jornadajava.spring_boot_trench.repository.SerieRepository;
import jornadajava.spring_boot_trench.request.SeriePostRequest;
import jornadajava.spring_boot_trench.request.SeriePutRequest;
import jornadajava.spring_boot_trench.response.SerieGetResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SerieService {

    private static final SerieMapper mapper = SerieMapper.MAPPER;
    private final SerieRepository repository;

    public SerieService(){
        this.repository = new SerieRepository();
    }

    public List<SerieGetResponse> findByName(String name){

        List<Serie> filteredList = repository.findByName(name);
        if (filteredList.isEmpty()){
            throw new RuntimeException("Serie com nome " + name + "não encontrado");
        }

        List<SerieGetResponse> getResponse = new ArrayList<>();
        for (Serie serie : filteredList){
            var dto = mapper.toSerieGetResponse(serie);
            getResponse.add(dto);
        }
        return getResponse;
    }

    public List<SerieGetResponse> getAll(){
        List<Serie> allList = repository.findAll();

        List<SerieGetResponse> responseList = new ArrayList<>();
        for (Serie serie : allList){
            var dto = mapper.toSerieGetResponse(serie);
            responseList.add(dto);
        }
        return responseList;
    }

    public SerieGetResponse findById(Long id){
        if (id == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "id não pode ser nulo");
        }
        java.util.Optional<Serie> optionalSerie = repository.findById(id);

        Serie serie = optionalSerie
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Id nao encontrado"));

        return mapper.toSerieGetResponse(serie);
    }

    public void delete(Long id){
        if (id == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "id nao pode ser nulo");
        }
        var serieOptional = repository.findById(id);

        Serie serieToDelete = serieOptional
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "id nao encontrado"));

        repository.delete(serieToDelete);
    }

    //neste metodo ele promete retornar um dto SerieGetResponse e recebe como parmetro um id e um dto SeriePutRequest
    public SerieGetResponse update(Long id, SeriePutRequest putRequest){

        //aqui é feita uma validação onde o id não pode ser nulo
        if (id == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "id nao pode ser nulo");
        }

        //serieOptional recebe o objeto do id requisitado pelo cliente
        var serieOptional = repository.findById(id);

        //aqui verificamos se serieOptional contem o objeto requisitado se sim ele se torna serieToUpdate, se nao, ele retorna um NOT FOUND
        Serie serieToUpdate = serieOptional.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "id nao encontrado"));

        //aqui estamos delegando para o mapper o ato de modificar o objeto,
        //serieToUpdate é oq vamos modificar e putRequest é oq estamos dizendo oq vai ser modificado
        mapper.SerieToUpdate(putRequest, serieToUpdate);

        //salvamos serieToUpdate no banco fake
        Serie updatedSerie = repository.save(serieToUpdate);

        //e retornamos um "recibo" dto do updatedSerie
        return mapper.toSerieGetResponse(updatedSerie);
    }

    //transformar um dto postRequest para dominio
    public SerieGetResponse save(SeriePostRequest postRequest){
        //um tratamento caso o id passado seja nulo
        if (postRequest == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "os dados para salvar nao podem ser nulos");
        }

        //o mapper faz o trabalho mano de atualizar cada atributo post request em dominio
        Serie serie = mapper.toSerie(postRequest);

        //no repository.save contem a logica de remover um livro antigo de mesmo id pelo novo de mesmo id
        Serie savedSERIE = repository.save(serie);

        //trazendo um ""
        return mapper.toSerieGetResponse(savedSERIE);
    }
}

package jornadajava.spring_boot_trench.service;

import jornadajava.spring_boot_trench.domain.Filme;
import jornadajava.spring_boot_trench.mapper.FilmeMapper;
import jornadajava.spring_boot_trench.repository.FilmeHardCodedRepository;
import jornadajava.spring_boot_trench.request.FilmePostRequest;
import jornadajava.spring_boot_trench.request.FilmePutRequest;
import jornadajava.spring_boot_trench.response.FilmeGetResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

//a organização do codigo mudou para o padrao MVC,
// onde cada função é separada,
// em service(o cerebro da API), 
// repository(onde fica armazenado os dados e os metodos para acessalos)
// e controller(que expoem essa API para a web)

@Slf4j
@Service
public class FilmeService {

    private final FilmeHardCodedRepository repository;
    private final FilmeMapper MAPPER = FilmeMapper.FILME_MAPPER;
    private long proximoID;

    public FilmeService(FilmeHardCodedRepository repository){
        this.repository = repository;
        this.proximoID = (long) repository.findAll().size() + 1;
    }

    public List<FilmeGetResponse> getAll(){
        List<FilmeGetResponse> responseList = new ArrayList<>();
        for (Filme filme : repository.findAll()){
            responseList.add(MAPPER.toFilmeGetResponse(filme));
        }
        return responseList;
    }

    public List<FilmeGetResponse> filterByname(String nome){
        List<FilmeGetResponse> filteredList = new ArrayList<>();
        if (nome == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "name not found");
        }
        for (Filme filme : repository.findAll()){
            if (filme.getNome().equalsIgnoreCase(nome)){
                filteredList.add(MAPPER.toFilmeGetResponse(filme));
            }
        }
        return filteredList;
    }

    public FilmeGetResponse save(FilmePostRequest request){
        Filme filme = MAPPER.toFilme(request);
        filme.setId(proximoID++);
        repository.save(filme);
        return MAPPER.toFilmeGetResponse(filme);
    }

    public void deleteById(Long id){
        Filme filme = repository.findByid(id);
        if (filme == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "id not found");
        }
        repository.delete(filme);
    }

    public void update(FilmePutRequest request, Long id){
        Filme filme = repository.findByid(id);
        if (filme == null){
            throw  new ResponseStatusException(HttpStatus.NOT_FOUND, "Filme not found");
        }
        MAPPER.FilmeToUpdate(request, filme);
        log.info("Filme updated: {}", filme);
    }
}

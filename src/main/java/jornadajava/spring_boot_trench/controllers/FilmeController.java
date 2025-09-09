package jornadajava.spring_boot_trench.controllers;

import jornadajava.spring_boot_trench.domain.Filme;
import jornadajava.spring_boot_trench.mapper.FilmeMapper;
import jornadajava.spring_boot_trench.request.FilmePostRequest;
import jornadajava.spring_boot_trench.request.FilmePutRequest;
import jornadajava.spring_boot_trench.response.FilmeGetResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("v1/filmes")
public class FilmeController {
    private Long proximoID;
    private static final FilmeMapper FILME_MAPPER = FilmeMapper.FILME_MAPPER;

    public FilmeController(){
        this.proximoID = (long) Filme.getFilmes().size() + 1;
    }


    @GetMapping
    public ResponseEntity<List<FilmeGetResponse>> getAll(){

        //primeiro: adicionar a fonte de dados no metodo
        var filmesDomain = Filme.getFilmes();

        //segundo: criando a lista DTO para copiar os dados da lista fonte
        List<FilmeGetResponse> getResponseList = new ArrayList<>();

        //terceiro: criamos um loop para lermos cada objeto da lista
        for (Filme filme : filmesDomain){

            //quarto: para cada objeto lido, copiamos ele para um DTO
            var DTO = FilmeMapper.FILME_MAPPER.toFilmeGetResponse(filme);

            //quinto: adicionamos cada copia na lista de resposta
            getResponseList.add(DTO);
        }
        //sexto: retornamos a lista desacoplada
        return ResponseEntity.ok(getResponseList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FilmeGetResponse> FindById(@PathVariable Long id){
        log.debug("requisição para identificar um id na lista {}", id);

        //primeiro: adicionar a fonte de dafos no metodo
        var listDomain = Filme.getFilmes();

        //segundo: verificando cada objeto da lista
        for (Filme filme : listDomain){

            //terceiro: identificando o id requisitado
            if (filme.getId().equals(id)){

                //quarto: copiando o objeto ligado ao id para o dto
                var dto = FilmeMapper.FILME_MAPPER.toFilmeGetResponse(filme);

                //quinto: retorna a lista desacoplada
                return ResponseEntity.ok(dto);
            }
        }
        //se nao achar o id requisitado, retorna um not found
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/filter")
    //como estamos definindo q o metodo é uma lista, precisamos devolver uma lista no return
    public ResponseEntity<List<FilmeGetResponse>> FilterList(@RequestParam String nome){
        log.debug("requisição para filtrar uma lista {}", nome);
        var domainList = Filme.getFilmes();
        List<FilmeGetResponse> getResponseList = new ArrayList<>();
        for (Filme filme : domainList){
            if (filme.getNome().equalsIgnoreCase(nome)){
                var dto = FilmeMapper.FILME_MAPPER.toFilmeGetResponse(filme);
                getResponseList.add(dto);
            }
        }
        //devolvendo a lista ResponseEntity<List<FilmeGetResponse>>
        return ResponseEntity.ok(getResponseList);
    }

    @PostMapping
    //no metodo estamos prometendo devolver uma resposta FilmeGetResponse e recebendo um parametro DTO postRequest
    public ResponseEntity<FilmeGetResponse> save(@RequestBody FilmePostRequest postRequest){
        log.debug("requisição para salvar um objeto na lista {}", postRequest);
        //primeiro: transformar o parametro dto em um objeto dominio
        var filme = FilmeMapper.FILME_MAPPER.toFilme(postRequest);
        //aqui o servidor esta definindo qual sera o id
        filme.setId(proximoID++);
        //segundo: cm o objeto dto transformado, adicionamos ele na lista de dominio
        Filme.getFilmes().add(filme);
        //terceiro: transformamos a resposta em dto como "recibo"
        var response = FilmeMapper.FILME_MAPPER.toFilmeGetResponse(filme);
        //quarto: retornamos o response dto
        return ResponseEntity.status(HttpStatus.CREATED).body(response);

        //é sempre necessario desacoplar todo dado, seja ele get ou post
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id){
        //primeiro: aqui ja abordamos um jeito diferente, estamos guardando um lugar para o id desejado
        Filme filmeToDelete = null;
        //segundo: percorremos a lista para encontrar o id desejado
        for (Filme filme : Filme.getFilmes()){
            if (filme.getId().equals(id)){
                //encontramos o id guardamos ele no "lugar" que criamos
                filmeToDelete = filme;
                //paramos o loop pois o id foi encontrado
                break;
            }
        }
        //isto é um tratamento de erro caso nao seja encontrado o id
        //se o filme para ser deletado for igual a null, lançamos um status de not found
        if (filmeToDelete == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Filme not found");
        }

        //terceiro: pegamos o id desejado que esta armazenado no filme to delete e removemos o mesmo da lista
        Filme.getFilmes().remove(filmeToDelete);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("{id}")
    public ResponseEntity<Void> update(@RequestBody FilmePutRequest request, @PathVariable Long id){
        log.debug("requisição para atualizar um filme na lista {}", request);
        Filme filmeToUpdate = null;
        for (Filme filme : Filme.getFilmes()){
            if (filme.getId().equals(id)){
                filmeToUpdate = filme;
                break;
            }
        }
        if (filmeToUpdate == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Filme not found");
        }
         FilmeMapper.FILME_MAPPER.FilmeToUpdate(request, filmeToUpdate);
        log.info("filme atualizado: {}", filmeToUpdate);

        return ResponseEntity.noContent().build();

        // DELETE e PUT Mapping se comportam de maneiras diferentes,
        // em ambos o metodos é necessario criar uma variavel para guardar o parametro passado pelo cliente
        //assim é precico iniciar um loop para percorrer a lista
        //parar o loop quando o parametro desejado é encontrado
        //e entao atribuir o parametro na variavel para guardar e modificar ela de acordo com o metodo

        //GET e POST Mapping não precisam disso pois estamos mexendo na lista como um todo
        //entao é só necessario ler a lista e aplicar a regra de negocio desejada
    }
}
  
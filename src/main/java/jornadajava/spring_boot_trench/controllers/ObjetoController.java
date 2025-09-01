package jornadajava.spring_boot_trench.controllers;

import jornadajava.spring_boot_trench.domain.Anime;
import jornadajava.spring_boot_trench.domain.Objeto;
import jornadajava.spring_boot_trench.mapper.ObjetoMapper;
import jornadajava.spring_boot_trench.request.ObjetoPostRequest;
import jornadajava.spring_boot_trench.response.ObjetoGetResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("v1/objeto")
public class ObjetoController {
    private Long proximoID;
    private static final ObjetoMapper MAPPER = ObjetoMapper.INSTANCE;

    public ObjetoController(){
        this.proximoID = (long) Objeto.getObjetos().size() + 1;
    }


    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE, headers = "api-key")
    public ResponseEntity<ObjetoGetResponse> save(@RequestBody ObjetoPostRequest objetoPostRequest,
                                                  @RequestHeader HttpHeaders headers){
        var produce = Objeto.builder()
                .ID((proximoID++))
                .atributo1(objetoPostRequest.getAtributo1()).build();
        produce.getObjetos().add(produce);

        ObjetoGetResponse response = ObjetoGetResponse.builder().
                ID(produce.getID()).
                atributo1(produce.getAtributo1()).
                build();
        log.info("save {}", produce);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("listAll")
    public ResponseEntity<List<ObjetoGetResponse>> listAll(){
        List<Objeto> responseDto = Objeto.getObjetos().stream().map
    }

    @GetMapping("{id}")
    public ResponseEntity<ObjetoGetResponse> findById(@PathVariable Long id){
        List<Objeto> objetos = Objeto.getObjetos();

        for (Objeto objeto : objetos){
            if (objeto.getID().equals(id)){
                var objetoGetresponse = MAPPER.toObjetoGetresponse(objeto);
                return ResponseEntity.ok(objetoGetresponse);
            }
        }
        return null;
    }
}

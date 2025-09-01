package jornadajava.spring_boot_trench.controllers;

import jornadajava.spring_boot_trench.domain.Producer;
import jornadajava.spring_boot_trench.mapper.ProducerMapper;
import jornadajava.spring_boot_trench.request.ProducerPostRequest;
import jornadajava.spring_boot_trench.response.ProducerGetResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("v1/producers")
public class ProducerController {
    private static final ProducerMapper MAPPER = ProducerMapper.INSTANCE;


    @GetMapping("filter")
    public List<Producer> lis(@RequestParam(required = false) String name) {
        var producers = Producer.getProducer();
        if (name == null) return producers;

        List<Producer> producersFilter = new ArrayList<>();
        //Cria um laço for each para inspecionar cada producer na lista producers
        for (Producer producer : producers){
            //se o nome na lista bater com o inserido pelo usuario, ele retorna o nome inserido
            if (producer.getNome().equalsIgnoreCase(name)){
                producersFilter.add(producer);
            }
        }
        return producersFilter;
    }

    @GetMapping("{id}")
    public ResponseEntity<ProducerGetResponse> findById(@PathVariable Long id){
        log.debug("Request to find produce by id: {}", id);
        var produceResponse = Producer.getProducer()
                .stream()
                .filter(producer -> producer.getId().equals(id))
                .findFirst()
                .map(MAPPER::toProducerGetResponse)
                .orElse(null);

        return ResponseEntity.ok(produceResponse);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE,
            consumes =MediaType.APPLICATION_JSON_VALUE,
            headers = "x-api-version")
    public ResponseEntity<ProducerGetResponse> save(
            @RequestBody ProducerPostRequest producerPostRequest,
            @RequestHeader HttpHeaders headers){

        Producer producer = MAPPER.toProducer(producerPostRequest);
        producer.getProducer().add(producer);
        var response = MAPPER.toProducerGetResponse(producer);



        log.info("save {}", producer);
        log.info("{}", headers);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<ProducerGetResponse>> listAll(@RequestParam(required = false) String name){
        log.debug("Request receive to all list all produces, param name {}", name);

        var producers = Producer.getProducer();
        var produceGetResonse = MAPPER.toProduceGetResponseLIST(producers);

        var response =
    }
}

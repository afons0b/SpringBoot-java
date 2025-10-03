package jornadajava.spring_boot_trench.controllers;

import jornadajava.spring_boot_trench.request.SeriePostRequest;
import jornadajava.spring_boot_trench.request.SeriePutRequest;
import jornadajava.spring_boot_trench.response.SerieGetResponse;
import jornadajava.spring_boot_trench.service.SerieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/serie")
@RequiredArgsConstructor
public class SerieController {
    private final SerieService service;

    @GetMapping
    public ResponseEntity<List<SerieGetResponse>> findAll(){
        List<SerieGetResponse> getSerie = service.getAll();
        return ResponseEntity.ok(getSerie);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<SerieGetResponse>> findByName(@RequestParam String name){
        List<SerieGetResponse> getFilterList = service.findByName(name);
        return ResponseEntity.ok(getFilterList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SerieGetResponse> findById(@PathVariable Long id){
        SerieGetResponse IdObject = service.findById(id);
        return ResponseEntity.ok(IdObject);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<SerieGetResponse> update(@PathVariable Long id, @RequestBody SeriePutRequest putRequest){
        SerieGetResponse updated = service.update(id, putRequest);

        return ResponseEntity.ok(updated);
    }

    @PostMapping
    public ResponseEntity<SerieGetResponse> save(@RequestBody SeriePostRequest postRequest){
        SerieGetResponse getResponse = service.save(postRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(getResponse);
    }

}

package jornadajava.spring_boot_trench.controllers;

import jornadajava.spring_boot_trench.response.SerieGetResponse;
import jornadajava.spring_boot_trench.service.SerieService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("v1/serie")
public class SerieController {

    private final SerieService service;

    public SerieController(){
        this.service = new SerieService();
    }

    @GetMapping
    public ResponseEntity<List<SerieGetResponse>> findall(){
        List<SerieGetResponse> getSerie = service.getAll();
        return ResponseEntity.ok(getSerie);
    }
}

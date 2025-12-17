package jornadajava.spring_boot_trench.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jornadajava.spring_boot_trench.config.BrasilApiConfigurationProperties;
import jornadajava.spring_boot_trench.response.CepGetResponse;
import jornadajava.spring_boot_trench.service.BrasilApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/brasil-api/cep")
@Log4j2
@RequiredArgsConstructor
@SecurityRequirement(name = "basciAuth")
public class BrasilApiController {
    private final BrasilApiService service;

    @GetMapping("/{cep}")
    public ResponseEntity<CepGetResponse>response(@PathVariable String cep){
        var cepGetResponse = service.findCep(cep);
        return ResponseEntity.ok(cepGetResponse);
    }
}

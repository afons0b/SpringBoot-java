package jornadajava.spring_boot_trench.controllers;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CsrfTokenController {
    @GetMapping("/csrf")
    public CsrfToken csrfToken(CsrfToken csrfToken){
        return csrfToken;
    }
}

package jornadajava.spring_boot_trench.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloControler {

    @GetMapping
    public String hi(){
        return "PRIMEIRO CONTROLLER";
    }
}

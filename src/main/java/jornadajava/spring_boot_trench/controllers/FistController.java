package jornadajava.spring_boot_trench.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("v1/greetings")
@Slf4j
public class FistController {

    @GetMapping("hi")
    public String hi() {
        return "PRIMEIRO CONTROLLER RUMO A VAGA DE BANCO";
    }


    @PostMapping
    public Long save (@RequestBody String name){
        log.info("save {}", name);
        return ThreadLocalRandom.current().nextLong(1, 1000);
    }

}

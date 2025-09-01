package jornadajava.spring_boot_trench.controllers;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("v1/heroes")
public class CharactersController {

    @GetMapping
    public List<String> listHeroes(){
        List<String> heroes = new ArrayList<>();
        heroes.add("GUts");
        heroes.add("Ichigo");
        heroes.add("Superman");
        return heroes;
    }

    @GetMapping("filter")
    public List<String> heroesFilter(@RequestParam String string){
        List<String> heroes = new ArrayList<>();
        for (String hero: listHeroes()){
            if (hero.equalsIgnoreCase(string)){
                heroes.add(hero);
            }
        }
        return heroes;
    }

    @GetMapping("{name}")
    public String findByName(@PathVariable String string){
        return listHeroes()
                .stream()
                .filter(hero -> hero.equalsIgnoreCase(string)).findFirst().orElse("");
    }
}

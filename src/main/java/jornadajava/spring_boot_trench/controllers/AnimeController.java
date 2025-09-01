package jornadajava.spring_boot_trench.controllers;

import jornadajava.spring_boot_trench.domain.Anime;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


@RestController
@RequestMapping("v1/anime")
public class AnimeController {

    @GetMapping("filter")
    public List<Anime> lis(@RequestParam(required = false) String name) {
        var animes = Anime.getAnime();
        if (name == null) return animes;

        List<Anime> animeFilter = new ArrayList<>();
        for (Anime anime : animes){
            if (anime.getNome().equalsIgnoreCase(name)){
                animeFilter.add(anime);
            }
        }
        return animeFilter;
    }

    @GetMapping("{id}")
    public Anime findById(@PathVariable long id){
        List<Anime> animes = Anime.getAnime();

        for (Anime anime : animes){
            if (anime.getId()==id){
                return anime;
            }
        }
    return null;
    }

    @PostMapping
    public Anime save(@RequestBody Anime anime){
       anime.setId(ThreadLocalRandom.current().nextLong(1000));
       Anime.getAnime().add(anime);
       return anime;
    }
}

package jornadajava.spring_boot_trench.domain;

import java.util.ArrayList;
import java.util.List;

public class Anime {

    private Long id;
    private String nome;
    private static List<Anime> anime = new ArrayList<>();

    static {
        var ninjaKamui = new Anime(1L, "Ninja Kamui");
        var kaijuuN08 = new Anime(2L, "Kaijuu No8");
        var naruto = new Anime(3L, "Naruto");
        anime.addAll(List.of(ninjaKamui, kaijuuN08, naruto));
    }

    public Anime(Long id, String name) {

        this.id = id;
        this.nome = name;
    }

    public Long getId() {
        return this.id;
    }

    public String getNome() {
        return this.nome;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public static List<Anime> getAnime() {
        return anime;
    }
}

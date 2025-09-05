package jornadajava.spring_boot_trench.repository;

import jornadajava.spring_boot_trench.domain.Filme;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class FilmeHardCodedRepository {

    private static final List<Filme> filmes = new ArrayList<>();

    static {
        var paradoxo = Filme.builder().id(1L).nome("Paradoxo").diretor("John kennedy").anoLancamento(2000).build();
        var dinossauro = Filme.builder().id(2L).nome("Dinossauro").diretor("Samuel menegati").anoLancamento(2010).build();
        var rock = Filme.builder().id(3L).nome("Rock Balboa").diretor("Silver Stalone").anoLancamento(1990).build();
        filmes.addAll(List.of(paradoxo, dinossauro, rock));

    }

    public  List<Filme> findAll(){
        return filmes;
    }

    public Filme findByid(Long id){
        for (Filme filme : findAll()){
            if (filme.getId().equals(id)){
                return filme;
            }
        }
        return null;
    }

    //parametro de nome filme do tipo Filme sendo salvado
    public void save(Filme filme){
        //pega o parametro filme e adiciona na lista filmes
        filmes.add(filme);
    }

    //um parametro de nome filme do tipo Filme sendo deletado
    public void delete(Filme filme){
        //pega o parametro filme e remove da lista filmes
        filmes.remove(filme);
    }
}

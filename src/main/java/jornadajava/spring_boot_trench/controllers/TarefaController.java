package jornadajava.spring_boot_trench.controllers;


import jornadajava.spring_boot_trench.domain.Tarefa;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("v1/tarefas")
public class TarefaController {

    private List<Tarefa> tarefas = new ArrayList<>();
    private Long proximoID = 1L;

    public TarefaController(){
        tarefas.add(new Tarefa(proximoID++, "Estudar spring boot", true));
        tarefas.add(new Tarefa(proximoID++, "Concluir o curso do willian", false));
        tarefas.add(new Tarefa(proximoID++, "Arrumar um estagio melhor", false));
    }

    @GetMapping
    public List<Tarefa> listarTudo(){
        return this.tarefas;
    }

    @GetMapping("/{id}")
    public Tarefa findByID(@PathVariable Long id){
        for (Tarefa tarefa: this.tarefas){
            if (tarefa.getID().equals(id)){
                return tarefa;
            }
        }
        return null;
    }

    @PostMapping
    public Tarefa adicionarTarefa(@RequestBody Tarefa tarefa){
        Tarefa novaTarefa = new Tarefa(proximoID++,tarefa.getDescricao(), false);
        this.tarefas.add(novaTarefa);
        return novaTarefa;
    }
}

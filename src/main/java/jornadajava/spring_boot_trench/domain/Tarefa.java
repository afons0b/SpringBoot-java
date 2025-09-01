package jornadajava.spring_boot_trench.domain;

import lombok.Getter;

import java.util.List;

@Getter
public class Tarefa {

    private Long ID;
    private String descricao;
    private boolean concluido;


    public Tarefa(long ID, String descricao, boolean concluido){
        this.ID = ID;
        this.descricao = descricao;
        this.concluido = concluido;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setConcluido(boolean concluido) {
        this.concluido = concluido;
    }

}

package jornadajava.spring_boot_trench.domain;

import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
public class Objeto {
    private Long ID;
    private String atributo1;
    private boolean atributo2;
    private static List<Objeto> objetos = new ArrayList<>();
    private LocalDateTime createdAt;
    private String teste;


    static {
        var objeto1 = Objeto.builder().ID(1L).atributo1("Caracteristica1").atributo2(false).createdAt(LocalDateTime.now()).build();
        var objeto2 = Objeto.builder().ID(2L).atributo1("Caracteristica2").atributo2(true).createdAt(LocalDateTime.now()).build();
        var objeto3 = Objeto.builder().ID(3L).atributo1("Caracteristica3").atributo2(false).createdAt(LocalDateTime.now()).build();
        objetos.addAll(List.of(objeto1, objeto2, objeto3));
    }

    public Long getID() {
        return ID;
    }

    public String getAtributo1() {
        return atributo1;
    }

    public boolean isAtributo2() {
        return atributo2;
    }

    public static List<Objeto> getObjetos() {
        return objetos;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public void setAtributo1(String atributo1) {
        this.atributo1 = atributo1;
    }

    public void setAtributo2(boolean atributo2) {
        this.atributo2 = atributo2;
    }

    public void setObjetos(List<Objeto> objetos) {
        this.objetos = objetos;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getTeste() {
        return teste;
    }

    public void setTeste(String teste) {
        this.teste = teste;
    }
}

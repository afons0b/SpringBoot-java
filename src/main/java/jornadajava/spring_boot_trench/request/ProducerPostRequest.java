package jornadajava.spring_boot_trench.request;

import lombok.Setter;


@Setter
public class ProducerPostRequest {
    private Long id;
    private String nome;

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }
}

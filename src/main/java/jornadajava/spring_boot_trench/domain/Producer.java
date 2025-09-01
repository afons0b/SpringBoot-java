package jornadajava.spring_boot_trench.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Builder
@Getter
@Setter
public class Producer {

    private Long id;
    private LocalDateTime createdAt;
    @JsonProperty("full_name")
    private String nome;
    private static List<Producer> producer = new ArrayList<>();


    static {
        var mappa =  Producer.builder().id(1L).nome("Produce1").createdAt(LocalDateTime.now()).build();
        var madhouse =  Producer.builder().id(2L).nome("Produce2").createdAt(LocalDateTime.now()).build();
        var kyotoAni =  Producer.builder().id(3L).nome("Produce3").createdAt(LocalDateTime.now()).build();
        producer.addAll(List.of(mappa, madhouse, kyotoAni));
    }

    public static List<Producer> getProducer() {
        return producer;
    }

}

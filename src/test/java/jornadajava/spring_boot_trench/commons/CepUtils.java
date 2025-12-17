package jornadajava.spring_boot_trench.commons;

import jornadajava.spring_boot_trench.response.CepGetResponse;
import org.springframework.stereotype.Component;

@Component
public class CepUtils {
    public CepGetResponse cepGetResponse(){
        return CepGetResponse.builder()
                .cep("000000")
                .city("Belem")
                .neighborhood("Sacramenta")
                .street("Santa Maria")
                .service("viacep")
                .build();
    }
}

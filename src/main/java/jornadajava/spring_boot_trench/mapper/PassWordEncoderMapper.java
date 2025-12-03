package jornadajava.spring_boot_trench.mapper;

import jornadajava.spring_boot_trench.annotation.EncodeMapping;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PassWordEncoderMapper {
    private final PasswordEncoder passwordEncoder;

    @EncodeMapping
    public String encode(String rawPassword){
        return passwordEncoder.encode(rawPassword);
    }
}

//o que eu quero fzr?
//codificar uma senha e ela vai vim em string

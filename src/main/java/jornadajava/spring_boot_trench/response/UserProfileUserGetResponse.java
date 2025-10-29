package jornadajava.spring_boot_trench.response;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserProfileUserGetResponse {
    @EqualsAndHashCode.Include
    private Long id;
    private String name;
    private int idade;
    private String lastName;
    private String email;

}

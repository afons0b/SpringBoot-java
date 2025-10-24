package jornadajava.spring_boot_trench.response;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProfilePostResponse {
    @EqualsAndHashCode.Include
    private Long id;
    private String name;
    private String description;

}

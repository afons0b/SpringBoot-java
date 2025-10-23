package jornadajava.spring_boot_trench.response;

import lombok.EqualsAndHashCode;

public class ProfileGetResponse {
    @EqualsAndHashCode.Include
    private Long id;
    private String name;
    private String description;
}

package jornadajava.spring_boot_trench.response;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserProfileGetResponse {
    @EqualsAndHashCode.Include
    private Long id;
    public record User(Long id, String name){

    }
    public record Profile(Long id, String name){

    }

    private User user;
    private Profile profile;
}

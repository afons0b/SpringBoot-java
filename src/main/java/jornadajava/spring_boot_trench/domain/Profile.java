package jornadajava.spring_boot_trench.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Profile {
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false, unique = true)
    private String name;
}

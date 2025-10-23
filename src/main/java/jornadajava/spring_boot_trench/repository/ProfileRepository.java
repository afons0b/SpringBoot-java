package jornadajava.spring_boot_trench.repository;

import jornadajava.spring_boot_trench.domain.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {

    Optional<Profile> findByNameIgnoreCase(String name);

}

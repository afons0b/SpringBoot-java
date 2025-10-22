package jornadajava.spring_boot_trench.repository;

import jornadajava.spring_boot_trench.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByNameIgnoreCase(String name);

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndIdNot(String email, Long id);
}

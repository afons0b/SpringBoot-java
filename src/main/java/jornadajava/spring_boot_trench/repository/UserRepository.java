package jornadajava.spring_boot_trench.repository;

import jornadajava.spring_boot_trench.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}

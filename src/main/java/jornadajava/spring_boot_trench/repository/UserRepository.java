package jornadajava.spring_boot_trench.repository;

import jornadajava.spring_boot_trench.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByNameIgnoreCase(String name);
}

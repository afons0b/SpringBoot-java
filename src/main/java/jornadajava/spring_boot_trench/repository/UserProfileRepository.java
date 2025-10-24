package jornadajava.spring_boot_trench.repository;

import jornadajava.spring_boot_trench.domain.User;
import jornadajava.spring_boot_trench.domain.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {}

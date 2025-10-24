package jornadajava.spring_boot_trench.service;

import jornadajava.spring_boot_trench.domain.Profile;
import jornadajava.spring_boot_trench.domain.UserProfile;
import jornadajava.spring_boot_trench.exception.NotFoundException;
import jornadajava.spring_boot_trench.mapper.ProfileMapper;
import jornadajava.spring_boot_trench.repository.ProfileRepository;
import jornadajava.spring_boot_trench.repository.UserProfileRepository;
import jornadajava.spring_boot_trench.request.ProfilePostRequest;
import jornadajava.spring_boot_trench.response.ProfileGetResponse;
import jornadajava.spring_boot_trench.response.ProfilePostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Log4j2
public class UserProfileService {

    private final UserProfileRepository repository;

    @Transactional(readOnly = true)
    public List<UserProfile> findAll(){
        return repository.findAll()
                .stream()
                .toList();
    }

}

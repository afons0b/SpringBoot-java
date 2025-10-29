package jornadajava.spring_boot_trench.service;

import jornadajava.spring_boot_trench.domain.UserProfile;
import jornadajava.spring_boot_trench.mapper.UserProfileMapper;
import jornadajava.spring_boot_trench.repository.UserProfileRepository;
import jornadajava.spring_boot_trench.response.UserProfileUserGetResponse;
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
    private final UserProfileMapper mapper;

    @Transactional(readOnly = true)
    public List<UserProfile> findAll(){
        return repository.findAll()
                .stream()
                .toList();
    }

    @Transactional
    public List<UserProfileUserGetResponse> findUsersByProfileId(Long id){
        return repository.findByProfileId(id)
                .stream()
                .map(UserProfile::getUser)
                .map(mapper::toUserProfileUserGetResponse)
                .toList();
    }

}

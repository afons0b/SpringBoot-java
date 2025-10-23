package jornadajava.spring_boot_trench.service;

import jornadajava.spring_boot_trench.domain.Profile;
import jornadajava.spring_boot_trench.exception.NotFoundException;
import jornadajava.spring_boot_trench.mapper.ProfileMapper;
import jornadajava.spring_boot_trench.repository.ProfileRepository;
import jornadajava.spring_boot_trench.request.ProfilePostRequest;
import jornadajava.spring_boot_trench.response.ProfileGetResponse;
import jornadajava.spring_boot_trench.response.ProfilePostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RequiredArgsConstructor
@Service
@Log4j2
public class ProfileService {

    private final ProfileRepository repository;
    private final ProfileMapper mapper;

    @Transactional(readOnly = true)
    public List<ProfileGetResponse> findAll(){
        return repository.findAll()
                .stream()
                .map(mapper::toProfileGetResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProfileGetResponse findById(Long id){
        return repository.findById(id)
                .map(mapper::toProfileGetResponse)
                .orElseThrow(()-> new NotFoundException("user not found or not exist"));
    }

    @Transactional(readOnly = true)
    public ProfileGetResponse findByName(String name){
        return repository.findByNameIgnoreCase(name)
                .map(mapper::toProfileGetResponse)
                .orElseThrow(()->new NotFoundException("this profile not exist or not found"));
    }

    @Transactional
    public ProfilePostResponse save(ProfilePostRequest postRequest){
        Profile profile = mapper.toProfile(postRequest);

        profile.setId(null);

        var savedProfile = repository.save(profile);
        log.info("profile created with id {}", savedProfile.getId());

        return mapper.toProfilePostResponse(savedProfile);
    }
}

package jornadajava.spring_boot_trench.controllers;

import jakarta.validation.Valid;
import jornadajava.spring_boot_trench.domain.UserProfile;
import jornadajava.spring_boot_trench.exception.ErrorDefaultMessage;
import jornadajava.spring_boot_trench.exception.NotFoundException;
import jornadajava.spring_boot_trench.mapper.UserProfileMapper;
import jornadajava.spring_boot_trench.request.UserPostRequest;
import jornadajava.spring_boot_trench.request.UserPutRequest;
import jornadajava.spring_boot_trench.response.*;
import jornadajava.spring_boot_trench.service.UserProfileService;
import jornadajava.spring_boot_trench.service.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/user-profile")
@RequiredArgsConstructor
public class UserProfileController {
    private final UserProfileService service;
    private final UserProfileMapper mapper;

    @GetMapping
    public ResponseEntity<List<UserProfileGetResponse>> findAll(){

        var userProfiles = service.findAll();
        var userProfileGetResponse = mapper.toUserProfileGetResponse(userProfiles);

        return ResponseEntity.ok(userProfileGetResponse);
    }

    @GetMapping("/profiles/{id}/users")
    public ResponseEntity<List<UserProfileUserGetResponse>> findAllUsersProfile(@PathVariable Long id){

        var userProfile = service.findUsersByProfileId(id);

        return ResponseEntity.ok(userProfile);
    }

}

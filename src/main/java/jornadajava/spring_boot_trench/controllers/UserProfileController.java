package jornadajava.spring_boot_trench.controllers;

import jakarta.validation.Valid;
import jornadajava.spring_boot_trench.domain.UserProfile;
import jornadajava.spring_boot_trench.exception.ErrorDefaultMessage;
import jornadajava.spring_boot_trench.exception.NotFoundException;
import jornadajava.spring_boot_trench.request.UserPostRequest;
import jornadajava.spring_boot_trench.request.UserPutRequest;
import jornadajava.spring_boot_trench.response.UserGetResponse;
import jornadajava.spring_boot_trench.response.UserPostResponse;
import jornadajava.spring_boot_trench.response.UserPutResponse;
import jornadajava.spring_boot_trench.service.UserProfileService;
import jornadajava.spring_boot_trench.service.UserService;
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

    @GetMapping
    public ResponseEntity<List<UserProfile>> findAll(){

        var userProfiles = service.findAll();

        return ResponseEntity.ok(userProfiles);
    }

}

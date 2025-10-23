package jornadajava.spring_boot_trench.controllers;

import jornadajava.spring_boot_trench.response.ProfileGetResponse;
import jornadajava.spring_boot_trench.response.UserGetResponse;
import jornadajava.spring_boot_trench.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("v1/profile")
@RequiredArgsConstructor
public class ProfileController {
    private ProfileService service;

    @GetMapping
    public ResponseEntity<List<ProfileGetResponse>> findAll(){

        List<ProfileGetResponse> getResponseList = service.findAll();

        return ResponseEntity.ok(getResponseList);
    }

    @GetMapping("/filter")
    public ResponseEntity<ProfileGetResponse> findByName(@RequestParam String name){

        ProfileGetResponse getNameList = service.findByName(name);

        return ResponseEntity.ok(getNameList);
    }
}

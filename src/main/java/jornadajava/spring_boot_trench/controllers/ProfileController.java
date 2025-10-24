package jornadajava.spring_boot_trench.controllers;

import jakarta.validation.Valid;
import jornadajava.spring_boot_trench.request.ProfilePostRequest;
import jornadajava.spring_boot_trench.response.ProfileGetResponse;
import jornadajava.spring_boot_trench.response.ProfilePostResponse;
import jornadajava.spring_boot_trench.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/profile")
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService service;

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

    @GetMapping("{id}")
    public ResponseEntity<ProfileGetResponse> findById(@PathVariable Long id){

        ProfileGetResponse getResponse = service.findById(id);

        return ResponseEntity.ok(getResponse);
    }

    @PostMapping
    public ResponseEntity<ProfilePostResponse> save(@RequestBody @Valid ProfilePostRequest postRequest) {

        ProfilePostResponse getPostResponse = service.save(postRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(getPostResponse);
    }
}

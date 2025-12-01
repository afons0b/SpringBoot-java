package jornadajava.spring_boot_trench.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jornadajava.spring_boot_trench.exception.ErrorDefaultMessage;
import jornadajava.spring_boot_trench.exception.NotFoundException;
import jornadajava.spring_boot_trench.request.UserPostRequest;
import jornadajava.spring_boot_trench.request.UserPutRequest;
import jornadajava.spring_boot_trench.response.UserGetResponse;
import jornadajava.spring_boot_trench.response.UserPostResponse;
import jornadajava.spring_boot_trench.response.UserPutResponse;
import jornadajava.spring_boot_trench.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/user")
@RequiredArgsConstructor
@SecurityRequirement(name = "basicAuth")
public class UserController {
    private final UserService service;

    @GetMapping
    @Operation(summary = "get all users", description = "get all users available in the system", responses = {
            @ApiResponse(description = "List all users",
                    responseCode = "200", content = @Content)
    })
    public ResponseEntity<List<UserGetResponse>> findAll(){

        List<UserGetResponse> getResponseList = service.findAll();

        return ResponseEntity.ok(getResponseList);
    }

    @GetMapping("/paginated")
    public ResponseEntity<Page<UserGetResponse>> findAllpaginated(Pageable pageable){

        var getResponseList = service.findAllPaginated(pageable);

        return ResponseEntity.ok(getResponseList);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<UserGetResponse>> findByName(@RequestParam String name){

        List<UserGetResponse> getNameList = service.findByName(name);

        return ResponseEntity.ok(getNameList);
    }

    @GetMapping("{id}")
    public ResponseEntity<UserGetResponse> findById(@PathVariable Long id){

        UserGetResponse getResponse = service.findById(id);

        return ResponseEntity.ok(getResponse);
    }

    @PostMapping
    public ResponseEntity<UserPostResponse> save(@RequestBody @Valid UserPostRequest postRequest){

        UserPostResponse getPostResponse = service.saveUser(postRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(getPostResponse);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){

        service.delete(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("{id}")
    public ResponseEntity<UserPutResponse> update(@PathVariable Long id, @RequestBody @Valid UserPutRequest putRequest){

        UserPutResponse getPutResponse = service.update(id, putRequest);

        return ResponseEntity.ok(getPutResponse);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorDefaultMessage> handleNotFoundMessage(NotFoundException exception){

        //atribuindo a variavel error o valor int do NOT FOUND status e a mensagem da classe NotFoundExcepetion
        var error = new ErrorDefaultMessage(HttpStatus.NOT_FOUND.value(), exception.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}

package jornadajava.spring_boot_trench.controllers;

import external.dependency.Connection;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/connection")
@RequiredArgsConstructor
public class ConnectionController {
    private final Connection connectionMySql;
    @GetMapping
    public ResponseEntity<Connection> getConnections(){
        return ResponseEntity.ok(connectionMySql);
    }
}

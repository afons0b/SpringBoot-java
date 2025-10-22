package jornadajava.spring_boot_trench.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.sql.SQLIntegrityConstraintViolationException;

@RestControllerAdvice
public class GlobalErrorHandlerAdvice {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorDefaultMessage> handleNotFoundMessage(NotFoundException exception){

        //atribuindo a variavel error o valor int do NOT FOUND status e a mensagem da classe NotFoundExcepetion
        var error = new ErrorDefaultMessage(HttpStatus.NOT_FOUND.value(), exception.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<ErrorDefaultMessage> SQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException exception){

        var error = new ErrorDefaultMessage(HttpStatus.BAD_REQUEST.value(), "ja existe um usuario com esse email");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorDefaultMessage> handleResponseStatusExcepetion(ResponseStatusException exception){

        var error = new ErrorDefaultMessage(exception.getStatusCode().value(), exception.getReason());

        return ResponseEntity.status(exception.getStatusCode()).body(error);
    }
}

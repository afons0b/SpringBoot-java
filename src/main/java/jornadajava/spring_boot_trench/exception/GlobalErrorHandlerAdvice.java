package jornadajava.spring_boot_trench.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalErrorHandlerAdvice {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorDefaultMessage> handleNotFoundMessage(NotFoundException exception){

        //atribuindo a variavel error o valor int do NOT FOUND status e a mensagem da classe NotFoundExcepetion
        var error = new ErrorDefaultMessage(HttpStatus.NOT_FOUND.value(), exception.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}

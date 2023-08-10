package tech.devinhouse.linhasaereas365.exceptions;

import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import tech.devinhouse.linhasaereas365.utils.ErroResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.time.Instant;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(CpfInvalidoException.class)
    public ResponseEntity<ErroResponse> handleCpfInvalidoException(CpfInvalidoException exception) {
        HttpStatus statusCode = HttpStatus.BAD_REQUEST;

        ErroResponse erroResponse = new ErroResponse(statusCode.value(), statusCode.getReasonPhrase(),
                exception.getMessage());

        return ResponseEntity.status(statusCode).body(erroResponse);
    }

    @ExceptionHandler(PassageiroJaCadastradoException.class)
    public ResponseEntity<ErroResponse> handlePassageiroJaCadastradoException(
            PassageiroJaCadastradoException exception) {
        HttpStatus statusCode = HttpStatus.CONFLICT;

        ErroResponse erroResponse = new ErroResponse(statusCode.value(), statusCode.getReasonPhrase(),
                exception.getMessage());

        return ResponseEntity.status(statusCode).body(erroResponse);
    }

    @ExceptionHandler(PassageiroInexistenteException.class)
    public ResponseEntity<ErroResponse> handlePassageiroInexistenteException(PassageiroInexistenteException ex) {
        HttpStatus statusCode = HttpStatus.NOT_FOUND;

        ErroResponse erroResponse = new ErroResponse(statusCode.value(), statusCode.getReasonPhrase(), ex.getMessage());

        return ResponseEntity.status(statusCode).body(erroResponse);
    }

    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<ErroResponse> handleNotFoundException(NotFoundException ex) {

        HttpStatus statusCode = HttpStatus.NOT_FOUND;

        ErroResponse erroResponse = new ErroResponse(statusCode.value(), statusCode.getReasonPhrase(), ex.getMessage());

        return ResponseEntity.status(statusCode).body(erroResponse);
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        Map<String, String> camposComErros = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String nomeDoCampo = ((FieldError) error).getField();
            String msgDeErro = error.getDefaultMessage();
            camposComErros.put(nomeDoCampo, msgDeErro);
        });
        ErroResponse erro = new ErroResponse(
                Instant.now(), 400, "MethodArgumentNotValidException",
                "Erro de validação", camposComErros);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex) {
        Map<String, String> camposComErros = new HashMap<>();
        ex.getConstraintViolations().forEach(e -> {
            Iterator<Path.Node> iterator = e.getPropertyPath().iterator();
            String nomeDoCampo = null;
            while (iterator.hasNext()) {
                nomeDoCampo = iterator.next().getName();
            }
            String msgDeErro = e.getMessage();
            camposComErros.put(nomeDoCampo, msgDeErro);
        });
        ErroResponse erro = new ErroResponse(
                Instant.now(), 400, "ConstraintViolationException",
                "Erro de validação", camposComErros);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    @ExceptionHandler(MalasNaoDespachadasException.class)
    public ResponseEntity<Object> handleFalhaExclusaoVeiculoComMultasException(MalasNaoDespachadasException e) {
        Map<String, String> retorno = new HashMap<>();
        retorno.put("erro", "nas fileiras 4 e 5 as malas devem ser despachadas");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(retorno);

    }
}

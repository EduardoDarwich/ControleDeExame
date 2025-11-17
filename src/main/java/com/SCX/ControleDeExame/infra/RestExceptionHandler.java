package com.SCX.ControleDeExame.infra;

import com.SCX.ControleDeExame.exception.CpfExistException;
import com.SCX.ControleDeExame.exception.CpfNotFoundException;
import com.SCX.ControleDeExame.exception.EmailExistException;
import com.SCX.ControleDeExame.exception.TelephoneExistException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EmailExistException.class)
    private ResponseEntity<String> emailExistHandler(EmailExistException e){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(TelephoneExistException.class)
    private ResponseEntity<String> telephoneExistHandler(TelephoneExistException e){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(CpfExistException.class)
    private ResponseEntity<String> cpfExistHandler(CpfExistException e){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(CpfNotFoundException.class)
    private ResponseEntity<String> cpfNotFoundHandler(CpfNotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}

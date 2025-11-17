package com.SCX.ControleDeExame.exception;

public class CpfNotFoundException extends RuntimeException {
    public CpfNotFoundException(){super("Paciente não cadastrado no sistema");}
    public CpfNotFoundException(String message) {
        super(message);
    }
}

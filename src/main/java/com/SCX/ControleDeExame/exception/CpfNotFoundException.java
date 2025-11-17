package com.SCX.ControleDeExame.exception;

public class CpfNotFoundException extends RuntimeException {

    //Metodo para tratar exceção específica do sistema
    public CpfNotFoundException(){super("Paciente não cadastrado no sistema");}
    public CpfNotFoundException(String message) {
        super(message);
    }
}

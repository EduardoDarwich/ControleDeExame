package com.SCX.ControleDeExame.exception;

public class CpfExistException extends RuntimeException {

    //Metodo para tratar exceção específica do sistema
    public CpfExistException(){super("Cpf ja cadastrado no sistema");}
    public CpfExistException(String message) {
        super(message);
    }
}

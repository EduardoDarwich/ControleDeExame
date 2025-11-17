package com.SCX.ControleDeExame.exception;

public class CpfExistException extends RuntimeException {
    public CpfExistException(){super("Cpf ja cadastrado no sistema");}
    public CpfExistException(String message) {
        super(message);
    }
}

package com.SCX.ControleDeExame.exception;

public class TelephoneExistException extends RuntimeException {

    //Metodo para tratar exceção específica do sistema
    public TelephoneExistException(){super("Telefone ja cadastrado no sistema");}
    public TelephoneExistException(String message) {
        super(message);
    }
}

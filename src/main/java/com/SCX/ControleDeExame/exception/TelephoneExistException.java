package com.SCX.ControleDeExame.exception;

public class TelephoneExistException extends RuntimeException {
    public TelephoneExistException(){super("Telefone ja cadastrado no sistema");}
    public TelephoneExistException(String message) {
        super(message);
    }
}

package com.SCX.ControleDeExame.exception;

public class EmailExistException extends RuntimeException{

    //Metodo para tratar exceção específica do sistema
    public EmailExistException(){super("Email ja cadastrado no sistema");}
    public EmailExistException(String message) {super(message);}
}

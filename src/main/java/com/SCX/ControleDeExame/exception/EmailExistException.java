package com.SCX.ControleDeExame.exception;

public class EmailExistException extends RuntimeException{
    public EmailExistException(){super("Email ja cadastrado no sistema");}
    public EmailExistException(String message) {super(message);}
}

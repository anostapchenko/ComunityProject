package com.javamentor.qa.platform.webapp.controllers.exceptions;

public class WrongPasswordFormatException extends RuntimeException{

    public WrongPasswordFormatException(String message) {
        super(message);
    }
}

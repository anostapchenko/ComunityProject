package com.javamentor.qa.platform.exception;

public class NoSuchDaoException extends RuntimeException {

    public NoSuchDaoException() {
    }

    public NoSuchDaoException(String message) {
        super(message);
    }
}

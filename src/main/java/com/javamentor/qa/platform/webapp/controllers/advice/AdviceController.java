package com.javamentor.qa.platform.webapp.controllers.advice;

import com.javamentor.qa.platform.exception.NoSuchDaoException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class AdviceController {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(NoSuchDaoException.class)
    public ResponseEntity<String> handleNoSuchDaoException(NoSuchDaoException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

}

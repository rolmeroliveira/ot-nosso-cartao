package com.zup.nossocartao.validacao;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class RestExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public List<FieldErrors> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        List<FieldErrors> listFieldErrors = new ArrayList<>();
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();

        fieldErrors.forEach(errors -> {
            String messageContext = messageSource.getMessage(errors, LocaleContextHolder.getLocale());
            FieldErrors error = new FieldErrors(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(),
                    HttpStatus.BAD_REQUEST.toString(), exception.getClass().toString(), errors.getField(), messageContext);
            listFieldErrors.add(error);
        });
        return listFieldErrors;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public StandardError handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
        return new StandardError(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.toString(),
                exception.getClass().toString());
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public StandardError handleHttpMessageNotReadableException(SQLIntegrityConstraintViolationException exception) {
        return new StandardError(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.toString(),
                exception.getLocalizedMessage());
    }

}

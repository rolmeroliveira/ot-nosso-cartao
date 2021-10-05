package com.zup.nossocartao.validacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    @ResponseStatus(code = HttpStatus.UNPROCESSABLE_ENTITY)
    public StandardError handleHttpMessageNotReadableException(SQLIntegrityConstraintViolationException exception) {
        return new StandardError(LocalDateTime.now(), HttpStatus.UNPROCESSABLE_ENTITY.value(), HttpStatus.UNPROCESSABLE_ENTITY.toString(),
                exception.getLocalizedMessage());
    }

    @ExceptionHandler(CustomNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public StandardError handleCustomNotFoundException(CustomNotFoundException exception) {
        return new StandardError(LocalDateTime.now(), HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.toString(),
                exception.getMsg());
    }

    @ExceptionHandler(CustomBusinesException.class)
    @ResponseStatus(code = HttpStatus.UNPROCESSABLE_ENTITY)
    public StandardError handleCustomNotOwnerException(CustomBusinesException exception) {
        return new StandardError(LocalDateTime.now(), HttpStatus.UNPROCESSABLE_ENTITY.value(), HttpStatus.UNPROCESSABLE_ENTITY.toString(),
                exception.getMsg());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public StandardError handleCustomInvalidValueOnField(ConstraintViolationException ex) {
        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
        String mensagem = "";

        for (ConstraintViolation<?> violation : violations) {
            mensagem += violation.getMessage();
        }
        return new StandardError(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.toString(),
                mensagem);
    }

}

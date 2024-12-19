package com.yibu.yibujudge.exceptions;

import com.yibu.yibujudge.model.response.Result;
import jakarta.validation.ConstraintViolationException;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@RestControllerAdvice
@Component
public class GlobalException {

    @ExceptionHandler(ConstraintViolationException.class)
    public Result<String> handleValidationExceptions(ConstraintViolationException ex) {
        String message = ex.getConstraintViolations().iterator().next().getMessage();
        return Result.error(message);
    }

    @ExceptionHandler(BaseException.class)
    public Result<String> handleBaseExceptions(BaseException ex) {
        ex.printStackTrace();
        return Result.error(ex.getMessage());
    }

    @ExceptionHandler(BindException.class)
    public Result<String> paramEx(BindException be) {
        return Result.error(Objects.requireNonNull(be.getBindingResult().getFieldError()).getDefaultMessage());
    }

    @ExceptionHandler(Exception.class)
    public Result<String> err(Exception e) {
        e.printStackTrace();
        return Result.error();
    }
}

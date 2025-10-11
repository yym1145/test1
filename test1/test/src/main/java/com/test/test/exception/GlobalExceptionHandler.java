package com.test.test.exception;

import com.test.test.result.Result;
import com.test.test.vo.ValidationError;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * 全局异常处理
 */

@ControllerAdvice
@Slf4j
@RequiredArgsConstructor
@Hidden
public class GlobalExceptionHandler {


    /**
     * 处理参数验证异常
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<Result<List<ValidationError>>> handleValidException(MethodArgumentNotValidException e){
        List<ValidationError> validationErrors = new ArrayList<>();
        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            ValidationError v = new ValidationError();
            v.setField(error.getField());
            v.setMessage(error.getDefaultMessage());
            validationErrors.add(v);
        }
        Result<List<ValidationError>> result = Result.error("字段验证错误",validationErrors);
        return new ResponseEntity<>(result, HttpStatus.UNPROCESSABLE_ENTITY);
    }


}

package com.redsun.server.gateway.controller.common;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionController {

	private Logger log = Logger.getLogger(GlobalExceptionController.class); 
	
    /**
     * method to handle bad request exception for all system
     * @param request
     * @param exception
     * @return
     */
    @ExceptionHandler(CustomBadRequestException.class)
    public ResponseEntity<?> handleBadRequestException(HttpServletRequest request, CustomBadRequestException exception){
        final HttpStatus status = exception.getCode() == null ? HttpStatus.BAD_REQUEST : exception.getCode();
        final String exceptionInfo = ExceptionUtils.getStackTrace(exception);
        
        // log.
        log.error(exceptionInfo);
        // return.
        final Map<String, Object> result = new HashMap<String, Object>();
        result.put("error", new ResponseEntity<Object>(exceptionInfo, status));
        return new ResponseEntity<Map<String, Object>>(result, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * method to handle all exception which does not have handler
     * @param request
     * @param exception
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneralException(HttpServletRequest request, Exception exception){
        final String exceptionInfo = ExceptionUtils.getStackTrace(exception);
        
        // log.
        log.error(exceptionInfo);
        // return.
        final Map<String, Object> result = new HashMap<String, Object>();
        result.put("error", new ResponseEntity<Object>(exceptionInfo, HttpStatus.INTERNAL_SERVER_ERROR));
        return new ResponseEntity<Map<String, Object>>(result, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * method to handle all sql exception for all system
     * @param request
     * @param exception
     * @return
     */
    @ExceptionHandler(CustomSQLException.class)
    public ResponseEntity<?> handleCustomSQLException(HttpServletRequest request, CustomSQLException exception){
        final HttpStatus status = exception.getCode() == null ? HttpStatus.INTERNAL_SERVER_ERROR : exception.getCode();
        final String exceptionInfo = ExceptionUtils.getStackTrace(exception);
        
       // log.
        log.error(exceptionInfo);
        // return.
        final Map<String, Object> result = new HashMap<String, Object>();
        result.put("error", new ResponseEntity<Object>(exceptionInfo, status));
        return new ResponseEntity<Map<String, Object>>(result, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Method to handle validation exception.
     * @param ex
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationError(MethodArgumentNotValidException exception){
        final BindingResult bindingResult = exception.getBindingResult();
        final List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        final String exceptionInfo = ExceptionUtils.getStackTrace(exception);
        
        // log.
        log.error(exceptionInfo);
        // return.
        final Map<String, Object> result = new HashMap<String, Object>();
        result.put("error", fieldErrors);
        return new ResponseEntity<Map<String, Object>>(result, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

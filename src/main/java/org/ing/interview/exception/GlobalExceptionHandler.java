package org.ing.interview.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidQueryParameters.class)
    public ResponseEntity handleInvalidQueryParametersException(InvalidQueryParameters exc) {
        return ResponseEntity.badRequest().body(exc.getErrorCodes());
    }
}

package org.example.handler;


import org.example.dto.ResponseInfo;
import org.example.exceptions.AlreadyExist;
import org.example.exceptions.NotFound;
import org.example.exceptions.NotMatch;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class BooklandExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleInvalidArgument(MethodArgumentNotValidException ex) {
        Map<String, String> errorMap = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errorMap.put(error.getField(), error.getDefaultMessage());
        });
        return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ResponseInfo> handleMissingRequestBody(HttpMessageNotReadableException ex) {
        ResponseInfo responseInfo = new ResponseInfo(null, false, "Not readable http request.(Likely your request body is empty)");
        return new ResponseEntity<>(responseInfo, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NotMatch.class)
    public ResponseEntity<ResponseInfo> handleNotMatchException(NotMatch ex) {
        ResponseInfo responseInfo = new ResponseInfo(null, false, ex.getMessage());
        return new ResponseEntity<>(responseInfo, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AlreadyExist.class)
    public ResponseEntity<ResponseInfo> handleAlreadyExistException(AlreadyExist ex) {
        ResponseInfo responseInfo = new ResponseInfo(null, false, ex.getMessage());
        return new ResponseEntity<>(responseInfo, HttpStatus.BAD_REQUEST);
    }


    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFound.class)
    public ResponseEntity<ResponseInfo> handleNotFoundedResource(NotFound ex) {
        ResponseInfo responseInfo = new ResponseInfo(null, false, ex.getMessage());
        return new ResponseEntity<>(responseInfo, HttpStatus.NOT_FOUND);
    }
}

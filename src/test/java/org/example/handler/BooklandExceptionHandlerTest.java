package org.example.handler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BooklandExceptionHandlerTest {
    MethodArgumentNotValidException ex;
    BooklandExceptionHandler handler = new BooklandExceptionHandler();

    @BeforeEach
    public void setup(){
        FieldError fieldError1 = Mockito.mock(FieldError.class);
        when(fieldError1.getField()).thenReturn("field1");
        when(fieldError1.getDefaultMessage()).thenReturn("message1");

        FieldError fieldError2 = Mockito.mock(FieldError.class);
        when(fieldError2.getField()).thenReturn("field2");
        when(fieldError2.getDefaultMessage()).thenReturn("message2");

        List<FieldError> fieldErrors = new ArrayList<>();
        fieldErrors.add(fieldError1);
        fieldErrors.add(fieldError2);

        BindingResult bindingResult = Mockito.mock(BindingResult.class);
        when(bindingResult.getFieldErrors()).thenReturn(fieldErrors);

        MethodParameter parameter = Mockito.mock(MethodParameter.class);
        ex = new MethodArgumentNotValidException(parameter, bindingResult);
    }

    @Test
    public void testHandleInvalidArgumentWith2Error(){
        ResponseEntity<Map<String, String>> resp = handler.handleInvalidArgument(ex);
        int status = resp.getStatusCode().value();
        Map<String, String> errors = resp.getBody();

        assertEquals(status, HttpStatus.BAD_REQUEST.value(), "Wrong status code");
        assertNotNull(errors, "Empty error list");
        assertEquals(errors.get("field1"), "message1", "Wrong error message");
        assertEquals(errors.get("field2"), "message2", "Wrong error message");
    }
}
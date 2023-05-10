package org.example.controller;

import org.example.dto.LoginForm;
import org.example.dto.ResponseInfo;
import org.example.dto.SignupForm;
import org.example.exceptions.AlreadyExist;
import org.example.exceptions.NotFound;
import org.example.exceptions.NotMatch;
import org.example.service.AuthService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.AbstractMap.SimpleEntry;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AuthControllerUnitTest {

    @Mock
    AuthService authService;

    @InjectMocks
    AuthController authController = new AuthController();

    @Mock
    LoginForm loginForm;
    @Mock
    SignupForm signupForm;

    @Test
    public void testLoginSuccessful() throws Exception{
        SimpleEntry<String, String> simpleEntry = new SimpleEntry<>("a", "b");
        when(authService.login(loginForm)).thenReturn(simpleEntry);

        ResponseEntity<ResponseInfo> res = authController.login(loginForm);

        assertEquals(res.getStatusCode(), HttpStatus.OK, "Status should should be ok");
        assertEquals(res.getBody().getValue(), simpleEntry, "The returned object of service should be in response");
    }
    @Test
    public void testLoginNotFound() throws Exception{
        when(authService.login(loginForm)).thenThrow(NotFound.class);

        assertThrows(NotFound.class,() -> {
            authController.login(loginForm);
        }, "Should throws NotFound");
    }
    @Test
    public void testLoginNotMatch() throws Exception{
        when(authService.login(loginForm)).thenThrow(NotMatch.class);

        assertThrows(NotMatch.class,() -> {
            authController.login(loginForm);
        }, "Should throws NotMatch");
    }
    @Test
    public void testLoginInternalServerError() throws Exception{
        when(authService.login(loginForm)).thenThrow(Exception.class);

        ResponseEntity<ResponseInfo> res = authController.login(loginForm);

        assertEquals(res.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR, "Status should should be internal server error");
        assertFalse(res.getBody().getSuccess(), "The success of response should be false");
    }

    @Test
    public void testSignupSuccessful() throws Exception{
        SimpleEntry<String, String> simpleEntry = new SimpleEntry<>("a", "b");
        when(authService.signup(signupForm)).thenReturn(simpleEntry);

        ResponseEntity<ResponseInfo> res = authController.signup(signupForm);

        assertEquals(res.getStatusCode(), HttpStatus.OK, "Status should should be ok");
        assertEquals(res.getBody().getValue(), simpleEntry, "The returned object of service should be in response");
    }
    @Test
    public void testSignupAlreadyExist() throws Exception{
        when(authService.signup(signupForm)).thenThrow(AlreadyExist.class);

        assertThrows(AlreadyExist.class,() -> {
            authController.signup(signupForm);
        }, "Should throws AlreadyExist");
    }
    @Test
    public void testSignupNotMatch() throws Exception{
        when(authService.signup(signupForm)).thenThrow(NotMatch.class);

        assertThrows(NotMatch.class,() -> {
            authController.signup(signupForm);
        }, "Should throws NotMatch");
    }
    @Test
    public void testSignupInternalServerError() throws Exception{
        when(authService.signup(signupForm)).thenThrow(Exception.class);

        ResponseEntity<ResponseInfo> res = authController.signup(signupForm);

        assertEquals(res.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR, "Status should should be internal server error");
        assertFalse(res.getBody().getSuccess(), "The success of response should be false");
    }
}

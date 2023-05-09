package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.LoginForm;
import org.example.dto.ResponseInfo;
import org.example.dto.SignupForm;
import org.example.service.AuthService;

import org.example.utils.Util;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.web.context.WebApplicationContext;

import java.util.*;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
class AuthControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;


    LoginForm loginForm;

    SignupForm signupForm;

    String JWT;

    @BeforeEach
    public void setUp() throws Exception {
        JWT = Util.createJWT("test@test.test", Util.getJWTKey());

        loginForm = new LoginForm();
        loginForm.setUserEmail("user1@gmail.com");
        loginForm.setPassword("pass");

        signupForm = new SignupForm();
        signupForm.setUserEmail("user1@gmail.com");
        signupForm.setPassword("pass");
        signupForm.setRepeatedPassword("pass");
    }


    @Test
    void IntegrationTestLoginSuccessful() throws Exception {
        AbstractMap.SimpleEntry<String, String> jwt = new AbstractMap.SimpleEntry<>("jwt", "jwt.jwt.jwt");
        when(authService.login(any())).thenReturn(jwt);

        ResponseInfo responseInfo = new ResponseInfo(jwt, true, "Login Successfully");
        String responseStr = objectMapper.writeValueAsString(responseInfo);

        RequestBuilder request= post("/auth/login").content(objectMapper.writeValueAsString(loginForm)).contentType("application/json")
                .header("auth-token", JWT);
        mvc.perform(request).andExpect(status().isOk()).andExpect(content().string(equalTo(responseStr)));
    }

    @Test
    void IntegrationTestLoginInternalServerError() throws Exception {
        when(authService.login(any())).thenThrow(Exception.class);

        ResponseInfo responseInfo = new ResponseInfo(null, false, "Internal Server Error");
        String responseStr = objectMapper.writeValueAsString(responseInfo);

        RequestBuilder request= post("/auth/login").content(objectMapper.writeValueAsString(loginForm)).contentType("application/json")
                .header("auth-token", JWT);
        mvc.perform(request).andExpect(status().isInternalServerError()).andExpect(content().string(equalTo(responseStr)));
    }


    @Test
    void IntegrationTestSignupSuccessful() throws Exception {
        AbstractMap.SimpleEntry<String, String> jwt = new AbstractMap.SimpleEntry<>("jwt", "jwt.jwt.jwt");
        when(authService.signup(any())).thenReturn(jwt);

        ResponseInfo responseInfo = new ResponseInfo(jwt, true, "Signup Successfully");
        String responseStr = objectMapper.writeValueAsString(responseInfo);

        RequestBuilder request= post("/auth/signup").content(objectMapper.writeValueAsString(signupForm)).contentType("application/json")
                .header("auth-token", JWT);
        mvc.perform(request).andExpect(status().isOk()).andExpect(content().string(equalTo(responseStr)));
    }

    @Test
    void IntegrationTestSignupInternalServerError() throws Exception {
        when(authService.signup(any())).thenThrow(Exception.class);

        ResponseInfo responseInfo = new ResponseInfo(null, false, "Internal Server Error");
        String responseStr = objectMapper.writeValueAsString(responseInfo);

        RequestBuilder request= post("/auth/signup").content(objectMapper.writeValueAsString(signupForm)).contentType("application/json")
                .header("auth-token", JWT);
        mvc.perform(request).andExpect(status().isInternalServerError()).andExpect(content().string(equalTo(responseStr)));
    }
}
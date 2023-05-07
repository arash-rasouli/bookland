package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.ResponseInfo;
import org.example.system.BooklandSystem;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.AbstractMap.SimpleEntry;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BooklandSystem booklandSystem;

    @Test
    void testLoginSuccessful() throws Exception {
        SimpleEntry<String, Object> entry = new SimpleEntry<>("jwt","a.b.c");
        when(booklandSystem.login(anyString(), anyString())).thenReturn(entry);

        ResponseInfo responseInfo = new ResponseInfo(entry, true, "Login Successfully");
        String responseStr = objectMapper.writeValueAsString(responseInfo);

        RequestBuilder request= MockMvcRequestBuilders.post("/auth/login?userEmail=aras&password=a");
        mvc.perform(request).andExpect(status().isOk()).andExpect(content().string(equalTo(responseStr)));

    }

    @Test
    void testLoginInvalidCredentials() throws Exception {
        when(booklandSystem.login(anyString(), anyString())).thenThrow(new Exception("message"));

        ResponseInfo responseInfo = new ResponseInfo(null, false, "Login Failed");
        responseInfo.addError("message");
        String responseStr = objectMapper.writeValueAsString(responseInfo);

        RequestBuilder request= MockMvcRequestBuilders.post("/auth/login?userEmail=aras&password=a");
        mvc.perform(request).andExpect(status().isUnauthorized()).andExpect(content().string(equalTo(responseStr)));

    }
}
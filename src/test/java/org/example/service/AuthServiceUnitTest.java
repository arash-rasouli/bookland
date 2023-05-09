package org.example.service;

import org.example.dao.UserRepository;
import org.example.dto.LoginForm;
import org.example.dto.SignupForm;
import org.example.exceptions.AlreadyExist;
import org.example.exceptions.NotFound;
import org.example.exceptions.NotMatch;
import org.example.model.User;
import org.example.utils.Util;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.AbstractMap.SimpleEntry;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
class AuthServiceUnitTest {

    @Mock
    UserRepository userRepo;

    @InjectMocks
    AuthService service = new AuthService();

    @Mock
    LoginForm loginForm;
    @Mock
    SignupForm signupForm;
    @Mock
    User user;

    private static MockedStatic<Util> mockedSettings;


    @BeforeAll
    public static void init() {
        mockedSettings = mockStatic(Util.class);
    }

    @AfterAll
    public static void close() {
        mockedSettings.close();
    }



    @Test
    void TestLoginUserNotFound() {
        when(loginForm.getUserEmail()).thenReturn("test@test.test");
        when(userRepo.findByEmail("test@test.test")).thenReturn(null);
        assertThrows(NotFound.class,() -> {
            service.login(loginForm);
        }, "Should return NotFound because user not found in database");
    }

    @Test
    void TestLoginUserInvalidCredential() {
        when(loginForm.getUserEmail()).thenReturn("test@test.test");
        when(loginForm.getPassword()).thenReturn("pass");
        when(userRepo.findByEmail("test@test.test")).thenReturn(user);
        when(user.verifyPassword("pass")).thenReturn(false);
        assertThrows(NotMatch.class,() -> {
            service.login(loginForm);
        }, "Should return NotMatch because of invalid credentials");
    }

    @Test
    void TestLoginUserSuccessful() throws Exception{
        when(loginForm.getUserEmail()).thenReturn("test@test.test");
        when(loginForm.getPassword()).thenReturn("pass");
        when(userRepo.findByEmail("test@test.test")).thenReturn(user);
        when(user.verifyPassword("pass")).thenReturn(true);

        when(Util.createJWT(eq("test@test.test"), any())).thenReturn("jwt.jwt.jwt");

        SimpleEntry<String, String> res = service.login(loginForm);
        assertEquals(res.getKey(), "jwt", "Key should be jwt");
        assertEquals(res.getValue(), "jwt.jwt.jwt", "Wrong jwt returned");
    }

    @Test
    void TestSignupUserAlreadyExist() {
        when(userRepo.findByEmail("test@test.test")).thenReturn(user);
        when(signupForm.getUserEmail()).thenReturn("test@test.test");
        assertThrows(AlreadyExist.class,() -> {
            service.signup(signupForm);
        }, "Should return AlreadyExist because user found in database");
    }

    @Test
    void TestSignupRepeatedPasswordNotMatch() {
        when(signupForm.getUserEmail()).thenReturn("test@test.test");
        when(signupForm.getPassword()).thenReturn("pass");
        when(signupForm.getRepeatedPassword()).thenReturn("pass1");
        when(userRepo.findByEmail("test@test.test")).thenReturn(null);
        assertThrows(NotMatch.class,() -> {
            service.signup(signupForm);
        }, "Should return NotMatch because repeated password doesn't match");
    }

    @Test
    void TestSignupSuccessfulVerifyMethodCalls() throws Exception{
        when(signupForm.getUserEmail()).thenReturn("test@test.test");
        when(signupForm.getPassword()).thenReturn("pass");
        when(signupForm.getRepeatedPassword()).thenReturn("pass");
        when(signupForm.initializeUser()).thenReturn(user);
        when(userRepo.findByEmail("test@test.test")).thenReturn(null);

        byte[] testByte = new byte[0];
        when(Util.getSHA("pass")).thenReturn(testByte);
        when(Util.hexToString(testByte)).thenReturn("hashed password");

        service.signup(signupForm);
        verify(user, description("user setPassword for setting hashed password should be called")).setPassword("hashed password");
        verify(userRepo, description("UserRepository save method should be called for saving new user")).save(user);
    }

    @Test
    void TestSignupSuccessfulCheckOutput() throws Exception{
        when(signupForm.getUserEmail()).thenReturn("test@test.test");
        when(signupForm.getPassword()).thenReturn("pass");
        when(signupForm.getRepeatedPassword()).thenReturn("pass");
        when(signupForm.initializeUser()).thenReturn(user);
        when(userRepo.findByEmail("test@test.test")).thenReturn(null);

        when(Util.createJWT(eq("test@test.test"), any())).thenReturn("jwt.jwt.jwt");

        SimpleEntry<String, String> res = service.signup(signupForm);
        assertEquals(res.getKey(), "jwt", "Key should be jwt");
        assertEquals(res.getValue(), "jwt.jwt.jwt", "Wrong jwt returned");

    }

}
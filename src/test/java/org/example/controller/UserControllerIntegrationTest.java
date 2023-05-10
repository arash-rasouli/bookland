package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.ResponseInfo;
import org.example.model.Book;
import org.example.service.UserService;
import org.example.utils.Util;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import java.util.*;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerIntegrationTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    String JWT;

    Book book1, book2;
    List<Book> books;

    @BeforeEach
    public void setUp() throws Exception {
        JWT = Util.createJWT("test@test.test", Util.getJWTKey());

        book1 = new Book();
        book1.setId(1);
        book1.setName("book1");
        book1.setAuthorName("author1");
        book1.setPublishedDate("2020-01-01");

        book2 = new Book();
        book2.setId(1);
        book2.setName("book2");
        book2.setAuthorName("author2");
        book2.setPublishedDate("2020-01-02");

        books= new ArrayList<>();
        books.add(book1);
        books.add(book2);
    }

    @Test
    void IntegrationTestGetAllBookInLibrarySuccessful() throws Exception {
        when(userService.getAllBookInLibrary("test@test.test")).thenReturn(books);

        ResponseInfo responseInfo = new ResponseInfo(books, true);
        String responseStr = objectMapper.writeValueAsString(responseInfo);

        RequestBuilder request= get("/user/library").header("auth-token", JWT);
        mvc.perform(request).andExpect(status().isOk()).andExpect(content().string(equalTo(responseStr)));
    }

    @Test
    void IntegrationTestGetAllBookInLibraryInternalServerError() throws Exception {
        when(userService.getAllBookInLibrary("test@test.test")).thenThrow(Exception.class);

        ResponseInfo responseInfo = new ResponseInfo(null, false, "Internal Server Error");
        String responseStr = objectMapper.writeValueAsString(responseInfo);

        RequestBuilder request= get("/user/library").header("auth-token", JWT);
        mvc.perform(request).andExpect(status().isInternalServerError()).andExpect(content().string(equalTo(responseStr)));
    }

    @Test
    void IntegrationTestGetBookInLibrarySuccessful() throws Exception {
        when(userService.getBookInLibrary("test@test.test", 1)).thenReturn(book1);

        ResponseInfo responseInfo = new ResponseInfo(book1, true);
        String responseStr = objectMapper.writeValueAsString(responseInfo);

        RequestBuilder request= get("/user/library/1").header("auth-token", JWT);
        mvc.perform(request).andExpect(status().isOk()).andExpect(content().string(equalTo(responseStr)));
    }

    @Test
    void IntegrationTestGetBookInLibraryInternalServerError() throws Exception {
        when(userService.getBookInLibrary("test@test.test", 1)).thenThrow(Exception.class);

        ResponseInfo responseInfo = new ResponseInfo(null, false, "Internal Server Error");
        String responseStr = objectMapper.writeValueAsString(responseInfo);

        RequestBuilder request= get("/user/library/1").header("auth-token", JWT);
        mvc.perform(request).andExpect(status().isInternalServerError()).andExpect(content().string(equalTo(responseStr)));
    }

    @Test
    void IntegrationTestAddBookToLibrarySuccessful() throws Exception {
        ResponseInfo responseInfo = new ResponseInfo(null, true, "Book Added To Library Successfully");
        String responseStr = objectMapper.writeValueAsString(responseInfo);

        RequestBuilder request= post("/user/library/1").header("auth-token", JWT);
        mvc.perform(request).andExpect(status().isOk()).andExpect(content().string(equalTo(responseStr)));
    }

    @Test
    void IntegrationTestAddBookToLibraryInternalServerError() throws Exception {
        doThrow(Exception.class).when(userService).addBookToLibrary("test@test.test", 1);

        ResponseInfo responseInfo = new ResponseInfo(null, false, "Internal Server Error");
        String responseStr = objectMapper.writeValueAsString(responseInfo);

        RequestBuilder request= post("/user/library/1").header("auth-token", JWT);
        mvc.perform(request).andExpect(status().isInternalServerError()).andExpect(content().string(equalTo(responseStr)));
    }

    @Test
    void IntegrationTestDeleteBookFromLibrarySuccessful() throws Exception {
        ResponseInfo responseInfo = new ResponseInfo(null, true, "Book Deleted From Library Successfully");
        String responseStr = objectMapper.writeValueAsString(responseInfo);

        RequestBuilder request= delete("/user/library/1").header("auth-token", JWT);
        mvc.perform(request).andExpect(status().isOk()).andExpect(content().string(equalTo(responseStr)));
    }

    @Test
    void IntegrationTestDeleteBookFromLibraryInternalServerError() throws Exception {
        doThrow(Exception.class).when(userService).deleteBookFromLibrary("test@test.test", 1);

        ResponseInfo responseInfo = new ResponseInfo(null, false, "Internal Server Error");
        String responseStr = objectMapper.writeValueAsString(responseInfo);

        RequestBuilder request= delete("/user/library/1").header("auth-token", JWT);
        mvc.perform(request).andExpect(status().isInternalServerError()).andExpect(content().string(equalTo(responseStr)));
    }
}
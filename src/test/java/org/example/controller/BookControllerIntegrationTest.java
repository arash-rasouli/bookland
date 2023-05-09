package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.ResponseInfo;
import org.example.model.Book;
import org.example.service.BookService;
import org.example.utils.Util;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
class BookControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookService bookService;

    Book book1, book2;

    String JWT;
    List<Book> books;

    @BeforeEach
    public void setup() throws Exception{
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

        JWT = Util.createJWT("test@test.test", Util.getJWTKey());
    }

    @Test
    void IntegrationTestGetAllBookSuccessful() throws Exception {
        when(bookService.getAllBooks()).thenReturn(books);

        ResponseInfo responseInfo = new ResponseInfo(books, true);
        String responseStr = objectMapper.writeValueAsString(responseInfo);

        RequestBuilder request= get("/book").header("auth-token", JWT);
        mvc.perform(request).andExpect(status().isOk()).andExpect(content().string(equalTo(responseStr)));
    }

    @Test
    void IntegrationTestGetBookAllInternalServerError() throws Exception {
        when(bookService.getAllBooks()).thenThrow(Exception.class);

        ResponseInfo responseInfo = new ResponseInfo(null, false, "Internal Server Error");
        String responseStr = objectMapper.writeValueAsString(responseInfo);

        RequestBuilder request= get("/book").header("auth-token", JWT);
        mvc.perform(request).andExpect(status().isInternalServerError()).andExpect(content().string(equalTo(responseStr)));
    }

    @Test
    void IntegrationTestGetBookSuccessful() throws Exception {
        when(bookService.getBook(1)).thenReturn(book1);

        ResponseInfo responseInfo = new ResponseInfo(book1, true);
        String responseStr = objectMapper.writeValueAsString(responseInfo);

        RequestBuilder request= get("/book/1").header("auth-token", JWT);
        mvc.perform(request).andExpect(status().isOk()).andExpect(content().string(equalTo(responseStr)));

    }

    @Test
    void IntegrationTestGetBookInternalServerError() throws Exception {
        when(bookService.getBook(1)).thenThrow(Exception.class);

        ResponseInfo responseInfo = new ResponseInfo(null, false, "Internal Server Error");
        String responseStr = objectMapper.writeValueAsString(responseInfo);

        RequestBuilder request= get("/book/1").header("auth-token", JWT);
        mvc.perform(request).andExpect(status().isInternalServerError()).andExpect(content().string(equalTo(responseStr)));
    }

    @Test
    void IntegrationTestAddBookSuccessful() throws Exception {
        when(bookService.addBook(any())).thenReturn(book1);
        ResponseInfo responseInfo = new ResponseInfo(book1, true, "Book Added Successfully");
        String responseStr = objectMapper.writeValueAsString(responseInfo);

        RequestBuilder request= post("/book").content(objectMapper.writeValueAsString(book1)).contentType("application/json")
                .header("auth-token", JWT);
        mvc.perform(request).andExpect(status().isCreated()).andExpect(content().string(equalTo(responseStr)));
    }

    @Test
    void IntegrationTestAddBookInternalServerError() throws Exception {
        when(bookService.addBook(any())).thenThrow(Exception.class);

        ResponseInfo responseInfo = new ResponseInfo(null, false, "Internal Server Error");
        String responseStr = objectMapper.writeValueAsString(responseInfo);

        RequestBuilder request= post("/book").content(objectMapper.writeValueAsString(book1)).contentType("application/json")
                .header("auth-token", JWT);
        mvc.perform(request).andExpect(status().isInternalServerError()).andExpect(content().string(equalTo(responseStr)));
    }

    @Test
    void IntegrationTestUpdatedBookSuccessful() throws Exception {
        when(bookService.updateBook(eq(1), any())).thenReturn(book1);

        ResponseInfo responseInfo = new ResponseInfo(book1, true, "Book Updated Successfully");
        String responseStr = objectMapper.writeValueAsString(responseInfo);

        RequestBuilder request= put("/book/1").content(objectMapper.writeValueAsString(book1)).contentType("application/json")
                .header("auth-token", JWT);
        mvc.perform(request).andExpect(status().isOk()).andExpect(content().string(equalTo(responseStr)));
    }

    @Test
    void IntegrationTestUpdatedBookInternalServerError() throws Exception {
        when(bookService.updateBook(eq(1), any())).thenThrow(Exception.class);

        ResponseInfo responseInfo = new ResponseInfo(null, false, "Internal Server Error");
        String responseStr = objectMapper.writeValueAsString(responseInfo);

        RequestBuilder request= put("/book/1").content(objectMapper.writeValueAsString(book1)).contentType("application/json")
                .header("auth-token", JWT);
        mvc.perform(request).andExpect(status().isInternalServerError()).andExpect(content().string(equalTo(responseStr)));
    }

    @Test
    void IntegrationTestDeleteBookSuccessful() throws Exception {
        ResponseInfo responseInfo = new ResponseInfo(null, true, "Book Deleted Successfully");
        String responseStr = objectMapper.writeValueAsString(responseInfo);

        RequestBuilder request= delete("/book/1").header("auth-token", JWT);
        mvc.perform(request).andExpect(status().isOk()).andExpect(content().string(equalTo(responseStr)));

    }

    @Test
    void IntegrationTestDeleteBookInternalServerError() throws Exception {
        doThrow(Exception.class).when(bookService).deleteBook(1);

        ResponseInfo responseInfo = new ResponseInfo(null, false, "Internal Server Error");
        String responseStr = objectMapper.writeValueAsString(responseInfo);

        RequestBuilder request= delete("/book/1").header("auth-token", JWT);
        mvc.perform(request).andExpect(status().isInternalServerError()).andExpect(content().string(equalTo(responseStr)));
    }
}
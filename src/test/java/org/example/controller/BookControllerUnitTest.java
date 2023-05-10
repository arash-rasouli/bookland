package org.example.controller;

import org.example.dto.BookForm;
import org.example.dto.ResponseInfo;
import org.example.exceptions.NotFound;
import org.example.model.Book;
import org.example.service.BookService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class BookControllerUnitTest {

    @Mock
    BookService bookService;

    @InjectMocks
    BookController bookController = new BookController();

    @Mock
    Book book;
    @Mock
    List<Book> books;

    @Mock
    BookForm bookForm;

    @Test
    public void testGetAllBookSuccessful() throws Exception{
        when(bookService.getAllBooks()).thenReturn(books);

        ResponseEntity<ResponseInfo> res = bookController.getAllBooks();

        assertEquals(res.getStatusCode(), HttpStatus.OK, "Status should should be ok");
        assertEquals(res.getBody().getValue(), books, "The returned object of service should be in response");
    }

    @Test
    public void testGetAllBookInternalServerError() throws Exception{
        when(bookService.getAllBooks()).thenThrow(Exception.class);

        ResponseEntity<ResponseInfo> res = bookController.getAllBooks();

        assertEquals(res.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR, "Status should should be internal server error");
        assertFalse(res.getBody().getSuccess(), "The success of response should be false");
    }

    @Test
    public void testGetBookSuccessful() throws Exception{
        when(bookService.getBook(123)).thenReturn(book);

        ResponseEntity<ResponseInfo> res = bookController.getBook(123);

        assertEquals(res.getStatusCode(), HttpStatus.OK, "Status should should be ok");
        assertEquals(res.getBody().getValue(), book, "The returned object of service should be in response");
    }

    @Test
    public void testGetBookInternalServerError() throws Exception{
        when(bookService.getBook(123)).thenThrow(Exception.class);

        ResponseEntity<ResponseInfo> res = bookController.getBook(123);

        assertEquals(res.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR, "Status should should be internal server error");
        assertFalse(res.getBody().getSuccess(), "The success of response should be false");
    }

    @Test
    public void testGetBookNotFound() throws Exception{
        when(bookService.getBook(123)).thenThrow(NotFound.class);

        assertThrows(NotFound.class,() -> {
            bookController.getBook(123);
        }, "Should throws NotFound");
    }

    @Test
    public void testAddBookSuccessful() throws Exception{

        when(bookService.addBook(bookForm)).thenReturn(book);

        ResponseEntity<ResponseInfo> res = bookController.addBook(bookForm);

        assertEquals(res.getStatusCode(), HttpStatus.CREATED, "Status should should be ok");
        assertEquals(res.getBody().getValue(), book, "The returned object of service should be in response");
    }

    @Test
    public void testAddBookInternalServerError() throws Exception{
        when(bookService.addBook(bookForm)).thenThrow(Exception.class);

        ResponseEntity<ResponseInfo> res = bookController.addBook(bookForm);

        assertEquals(res.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR, "Status should should be internal server error");
        assertFalse(res.getBody().getSuccess(), "The success of response should be false");
    }

    @Test
    public void testDeleteBookSuccessful() throws Exception{
        ResponseEntity<ResponseInfo> res = bookController.deleteBook(123);

        assertEquals(res.getStatusCode(), HttpStatus.OK, "Status should should be ok");
        verify(bookService, description("userService's deleteBookFromLibrary method should be called")).deleteBook(123);

    }

    @Test
    public void testDeleteBookInternalServerError() throws Exception{
        doThrow(Exception.class).when(bookService).deleteBook(123);

        ResponseEntity<ResponseInfo> res = bookController.deleteBook(123);

        assertEquals(res.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR, "Status should should be internal server error");
        assertFalse(res.getBody().getSuccess(), "The success of response should be false");
    }

    @Test
    public void testDeleteBookNotFound() throws Exception{
        doThrow(NotFound.class).when(bookService).deleteBook(123);

        assertThrows(NotFound.class,() -> {
            bookController.deleteBook(123);
        }, "Should throws NotFound");
    }
}

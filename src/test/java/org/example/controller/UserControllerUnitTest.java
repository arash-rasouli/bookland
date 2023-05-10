package org.example.controller;

import org.example.dto.ResponseInfo;
import org.example.exceptions.NotFound;
import org.example.exceptions.NotMatch;
import org.example.model.Book;
import org.example.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.AbstractMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserControllerUnitTest {
    @Mock
    UserService userService;

    @InjectMocks
    UserController userController = new UserController();

    @Mock
    Book book;
    @Mock
    List<Book> books;

    @Test
    public void testGetAllBookInLibrarySuccessful() throws Exception{
        when(userService.getAllBookInLibrary("test@test.test")).thenReturn(books);

        ResponseEntity<ResponseInfo> res = userController.getAllBookInLibrary("test@test.test");

        assertEquals(res.getStatusCode(), HttpStatus.OK, "Status should should be ok");
        assertEquals(res.getBody().getValue(), books, "The returned object of service should be in response");
    }

    @Test
    public void testGetAllBookInLibraryInternalServerError() throws Exception{
        when(userService.getAllBookInLibrary("test@test.test")).thenThrow(Exception.class);

        ResponseEntity<ResponseInfo> res = userController.getAllBookInLibrary("test@test.test");

        assertEquals(res.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR, "Status should should be internal server error");
        assertFalse(res.getBody().getSuccess(), "The success of response should be false");
    }

    @Test
    public void testGetBookInLibrarySuccessful() throws Exception{
        when(userService.getBookInLibrary("test@test.test",123)).thenReturn(book);

        ResponseEntity<ResponseInfo> res = userController.getBookInLibrary("test@test.test",123);

        assertEquals(res.getStatusCode(), HttpStatus.OK, "Status should should be ok");
        assertEquals(res.getBody().getValue(), book, "The returned object of service should be in response");
    }

    @Test
    public void testGetBookInLibraryInternalServerError() throws Exception{
        when(userService.getBookInLibrary("test@test.test",123)).thenThrow(Exception.class);

        ResponseEntity<ResponseInfo> res = userController.getBookInLibrary("test@test.test",123);

        assertEquals(res.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR, "Status should should be internal server error");
        assertFalse(res.getBody().getSuccess(), "The success of response should be false");
    }

    @Test
    public void testGetBookInLibraryNotFound() throws Exception{
        when(userService.getBookInLibrary("test@test.test",123)).thenThrow(NotFound.class);

        assertThrows(NotFound.class,() -> {
            userController.getBookInLibrary("test@test.test",123);
        }, "Should throws NotFound");
    }

    @Test
    public void testAddBookToLibrarySuccessful() throws Exception{
        ResponseEntity<ResponseInfo> res = userController.addBookToLibrary("test@test.test",123);

        assertEquals(res.getStatusCode(), HttpStatus.OK, "Status should should be ok");
        verify(userService, description("userService's addBookToLibrary method should be called")).addBookToLibrary("test@test.test",123);
    }

    @Test
    public void testAddBookToLibraryInternalServerError() throws Exception{
        doThrow(Exception.class).when(userService).addBookToLibrary("test@test.test",123);

        ResponseEntity<ResponseInfo> res = userController.addBookToLibrary("test@test.test",123);

        assertEquals(res.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR, "Status should should be internal server error");
        assertFalse(res.getBody().getSuccess(), "The success of response should be false");
    }

    @Test
    public void testAddBookToLibraryNotFound() throws Exception{
        doThrow(NotFound.class).when(userService).addBookToLibrary("test@test.test",123);

        assertThrows(NotFound.class,() -> {
            userController.addBookToLibrary("test@test.test",123);
        }, "Should throws NotFound");
    }

    @Test
    public void testDeleteBookFromLibrarySuccessful() throws Exception{
        ResponseEntity<ResponseInfo> res = userController.deleteBookFromLibrary("test@test.test",123);

        assertEquals(res.getStatusCode(), HttpStatus.OK, "Status should should be ok");
        verify(userService, description("userService's deleteBookFromLibrary method should be called")).deleteBookFromLibrary("test@test.test",123);
    }

    @Test
    public void testDeleteBookFromLibraryInternalServerError() throws Exception{
        doThrow(Exception.class).when(userService).deleteBookFromLibrary("test@test.test",123);

        ResponseEntity<ResponseInfo> res = userController.deleteBookFromLibrary("test@test.test",123);

        assertEquals(res.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR, "Status should should be internal server error");
        assertFalse(res.getBody().getSuccess(), "The success of response should be false");
    }

    @Test
    public void testDeleteBookFromLibraryNotFound() throws Exception{
        doThrow(NotFound.class).when(userService).deleteBookFromLibrary("test@test.test",123);

        assertThrows(NotFound.class,() -> {
            userController.deleteBookFromLibrary("test@test.test",123);
        }, "Should throws NotFound");
    }
}

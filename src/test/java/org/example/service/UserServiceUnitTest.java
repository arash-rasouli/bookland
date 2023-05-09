package org.example.service;

import org.example.dao.BookRepository;
import org.example.dao.UserRepository;
import org.example.exceptions.AlreadyExist;
import org.example.exceptions.NotFound;
import org.example.model.Book;
import org.example.model.User;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceUnitTest {
    @Mock
    private BookRepository bookRepo;

    @Mock
    private UserRepository userRepo;

    @InjectMocks
    private UserService service = new UserService();

    @Mock
    User user1;

    @Mock
    Book book;
    @Mock
    List<Book> books;


    @Test
    void testGetAllBookInLibraryUserNotFound() {
        when(userRepo.findByEmail("email")).thenReturn(null);
        assertThrows(Exception.class,() -> {
            service.getAllBookInLibrary("email");
        }, "Should return Exception because authenticated user not found in database");
    }

    @Test
    void testGetAllBookInLibrarySuccessful() throws Exception{
        when(userRepo.findByEmail("email")).thenReturn(user1);
        when(user1.getLibrary()).thenReturn(books);
        assertEquals(books, service.getAllBookInLibrary("email"), "Should return user library");
    }

    @Test
    void testGetBookInLibraryUserNotFound() {
        when(userRepo.findByEmail("email")).thenReturn(null);
        assertThrows(Exception.class,() -> {
            service.getBookInLibrary("email",1);
        }, "Should return Exception because authenticated user not found in database");
    }

    @Test
    void testGetBookInLibraryBookNotFound() {
        when(userRepo.findByEmail("email")).thenReturn(user1);
        when(user1.getBookFromLibrary(1)).thenReturn(null);
        assertThrows(NotFound.class,() -> {
            service.getBookInLibrary("email", 1);
        }, "Should return NotFound because book not found in library");
    }

    @Test
    void testGetBookInLibrarySuccessful() throws Exception{
        when(userRepo.findByEmail("email")).thenReturn(user1);
        when(user1.getBookFromLibrary(1)).thenReturn(book);
        assertEquals(book, service.getBookInLibrary("email", 1), "Should return the specific book");
    }

    @Test
    void testAddBookToLibraryUserNotFound() {
        when(userRepo.findByEmail("email")).thenReturn(null);
        assertThrows(Exception.class,() -> {
            service.addBookToLibrary("email", 1);
        }, "Should return Exception because authenticated user not found in database");
    }

    @Test
    void testAddBookToLibraryBookAlreadyExist() {
        when(userRepo.findByEmail("email")).thenReturn(user1);
        when(user1.getBookFromLibrary(1)).thenReturn(new Book());
        assertThrows(AlreadyExist.class,() -> {
            service.addBookToLibrary("email", 1);
        }, "Should return AlreadyExist because book found in library");
    }

    @Test
    void testAddBookToLibraryBookNotFoundInRepo() {
        when(userRepo.findByEmail("email")).thenReturn(user1);
        when(user1.getBookFromLibrary(1)).thenReturn(null);
        when(bookRepo.findById(1)).thenReturn(Optional.empty());
        assertThrows(NotFound.class,() -> {
            service.addBookToLibrary("email", 1);
        }, "Should return NotFound because book found in repository");
    }

    @Test
    void testAddBookToLibrarySuccessful() throws Exception{
        when(userRepo.findByEmail("email")).thenReturn(user1);
        when(user1.getBookFromLibrary(1)).thenReturn(null);
        when(bookRepo.findById(1)).thenReturn(Optional.of(book));
        service.addBookToLibrary("email", 1);
        verify(user1, description("user addBookToLibrary should be called")).addBookToLibrary(book);
        verify(userRepo, description("userRepo save should be called")).save(user1);
    }

    @Test
    void testDeleteBookFromLibraryUserNotFound() {
        when(userRepo.findByEmail("email")).thenReturn(null);
        assertThrows(Exception.class,() -> {
            service.deleteBookFromLibrary("email", 1);
        }, "Should return Exception because authenticated user not found in database");
    }

    @Test
    void testDeleteBookFromLibraryBookNotFound() {
        when(userRepo.findByEmail("email")).thenReturn(user1);
        when(user1.getBookFromLibrary(1)).thenReturn(null);
        assertThrows(NotFound.class,() -> {
            service.deleteBookFromLibrary("email", 1);
        }, "Should return NotFound because book not found in library");
    }

    @Test
    void testDeleteBookFromLibrarySuccessful() throws Exception{
        when(userRepo.findByEmail("email")).thenReturn(user1);
        when(user1.getBookFromLibrary(1)).thenReturn(new Book());
        service.deleteBookFromLibrary("email", 1);
        verify(user1, description("user deleteBookFromLibrary should be called")).deleteBookFromLibrary(1);
        verify(userRepo, description("userRepo save should be called")).save(user1);
    }

}
package org.example.service;

import org.example.dao.BookRepository;
import org.example.exceptions.NotFound;
import org.example.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class BookServiceTest {

    @Mock
    private BookRepository repo;

    @Mock
    private Book book1, book2;

    private ArrayList<Book> books;

    @InjectMocks
    private BookService service = new BookService();

    @BeforeEach
    public void setup(){
        books = new ArrayList<>();
    }

    @Test
    void testGetAllBooksEmpty() {
        when(repo.findAll()).thenReturn(books);
        assertArrayEquals(books.toArray(), service.getAllBooks().toArray());
    }

    @Test
    void testGetAllBooksHas2Element() {
        books.add(book1);
        books.add(book2);
        when(repo.findAll()).thenReturn(books);
        assertArrayEquals(books.toArray(), service.getAllBooks().toArray());
    }

    @Test
    void testAddBook() {
        when(repo.save(book1)).thenReturn(any());
        service.addBook(book1);
        verify(repo).save(book1);
    }

    @Test
    void testGetBookNotFound() throws NotFound {
        when(repo.findById(11)).thenReturn(Optional.empty());
        assertThrows(NotFound.class,() -> {
            service.getBook(11);
        }, "Should return Not Found Exception");
    }

    @Test
    void testGetBookFound() throws NotFound {
        when(repo.findById(11)).thenReturn(Optional.of(book1));
        assertEquals(book1, service.getBook(11), "Should return the book object");
    }

    @Test
    void testUpdateBookNotFound() throws NotFound {
        when(repo.findById(11)).thenReturn(Optional.empty());
        assertThrows(NotFound.class,() -> {
            service.updateBook(11, book1);
        }, "Should return Not Found Exception");
    }

    @Test
    void testUpdateBookReturnNewBook() throws NotFound {
        when(repo.findById(11)).thenReturn(Optional.of(book1));
        assertEquals(book2, service.updateBook(11, book2), "Should update and return the new book object");
    }

    @Test
    void testUpdateBookVerifyFunctionShouldBeCalled() throws NotFound {
        when(repo.findById(11)).thenReturn(Optional.of(book1));
        service.updateBook(11,book2);
        verify(book1, description("Update method of book should be called")).update(book2);
        verify(repo, description("save method of Repository should be called")).save(book1);
    }

    @Test
    void testDeleteBookNotFound() throws NotFound {
        when(repo.findById(11)).thenReturn(Optional.empty());
        assertThrows(NotFound.class,() -> {
            service.deleteBook(11);
        }, "Should return Not Found Exception");
    }

    @Test
    void testDeleteBookVerifyFunctionShouldBeCalled() throws NotFound {
        when(repo.existsById(11)).thenReturn(true);
        service.deleteBook(11);
        verify(repo, description("deleteById method of Repository should be called")).deleteById(11);
    }
}
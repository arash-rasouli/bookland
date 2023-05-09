package org.example.service;

import org.example.dao.BookRepository;
import org.example.dto.BookForm;
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
class BookServiceUnitTest {

    @Mock
    private BookRepository repo;

    @Mock
    private Book book1, book2;

    @Mock
    private BookForm bookForm1, bookForm2;

    private ArrayList<Book> books;

    @InjectMocks
    private BookService service = new BookService();

    @BeforeEach
    public void setup(){
        when(bookForm1.initializeBook()).thenReturn(book1);
        when(bookForm2.initializeBook()).thenReturn(book2);
        books = new ArrayList<>();
    }

    @Test
    void testGetAllBooksEmpty() throws Exception{
        when(repo.findAll()).thenReturn(books);
        assertEquals(0, service.getAllBooks().size(), "Should return empty array");
    }

    @Test
    void testGetAllBooksHas2Element() throws Exception{
        books.add(book1);
        books.add(book2);
        when(repo.findAll()).thenReturn(books);
        assertArrayEquals(books.toArray(), service.getAllBooks().toArray(), "Arrays should be equals");
    }

    @Test
    void testAddBook() throws Exception{
        service.addBook(bookForm1);
        verify(repo, description("book save method should be called")).save(book1);
    }

    @Test
    void testGetBookNotFound() throws Exception {
        when(repo.findById(11)).thenReturn(Optional.empty());
        assertThrows(NotFound.class,() -> {
            service.getBook(11);
        }, "Should return Not Found Exception");
    }

    @Test
    void testGetBookFound() throws Exception {
        when(repo.findById(11)).thenReturn(Optional.of(book1));
        assertEquals(book1, service.getBook(11), "Should return the book object");
    }

    @Test
    void testUpdateBookNotFound() throws Exception {
        when(repo.findById(11)).thenReturn(Optional.empty());
        assertThrows(NotFound.class,() -> {
            service.updateBook(11, bookForm1);
        }, "Should return Not Found Exception");
    }

    @Test
    void testUpdateBookReturnNewBook() throws Exception {
        when(repo.findById(11)).thenReturn(Optional.of(book1));
        assertEquals(book2, service.updateBook(11, bookForm2), "Should update and return the new book object");
    }

    @Test
    void testUpdateBookVerifyFunctionShouldBeCalled() throws Exception {
        when(repo.findById(11)).thenReturn(Optional.of(book1));
        service.updateBook(11,bookForm2);
        verify(book1, description("Update method of book should be called")).update(book2);
        verify(repo, description("save method of Repository should be called")).save(book1);
    }

    @Test
    void testDeleteBookNotFound() throws Exception {
        when(repo.findById(11)).thenReturn(Optional.empty());
        assertThrows(NotFound.class,() -> {
            service.deleteBook(11);
        }, "Should return Not Found Exception");
    }

    @Test
    void testDeleteBookVerifyFunctionShouldBeCalled() throws Exception {
        when(repo.existsById(11)).thenReturn(true);
        service.deleteBook(11);
        verify(repo, description("deleteById method of Repository should be called")).deleteById(11);
    }
}
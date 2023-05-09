package org.example.model;

import org.example.utils.Util;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserTest {

    List<Book> books = new ArrayList<>();
    @Mock
    Book book1, book2;
    User user = new User("email", "pass");

    @BeforeEach
    public void setup(){
        MockitoAnnotations.initMocks(this);

        books = new ArrayList<>();
        user.setLibrary(books);
    }

    @Test
    void testAddBookToLibrary() {
        user.addBookToLibrary(book1);
        assertTrue(books.contains(book1), "book should be added");
    }

    @Test
    void testGetBookFromLibraryFound() {
        books.add(book1);
        when(book1.getId()).thenReturn(4);
        assertEquals(user.getBookFromLibrary(4), book1, "specific book should be returned");
    }

    @Test
    void testGetBookFromLibraryNotFound() {
        books.add(book1);
        when(book1.getId()).thenReturn(4);
        assertNull(user.getBookFromLibrary(5), "null should be returned");
    }

    @Test
    void testDeleteBookFromLibrary() {
        books.add(book1);
        books.add(book2);
        when(book1.getId()).thenReturn(4);
        when(book2.getId()).thenReturn(5);
        user.deleteBookFromLibrary(4);
        assertTrue(!books.contains(book1) && books.contains(book2), "just specific book should be deleted");
    }

    @Test
    void verifyPassword() {
        try (MockedStatic mocked = mockStatic(Util.class)) {
            byte[] testByte = new byte[0];
            when(Util.getSHA("unHashed password")).thenReturn(testByte);
            when(Util.hexToString(testByte)).thenReturn("pass");
            assertTrue(user.verifyPassword("unHashed password"));
        }
    }
}
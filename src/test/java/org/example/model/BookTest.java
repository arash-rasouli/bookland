package org.example.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookTest {

    @Test
    void testUpdate() {
        Book book = new Book();
        Book _book = new Book();
        _book.setName("name1");
        _book.setAuthorName("author1");
        _book.setPublishedDate("date1");

        book.update(_book);
        assertEquals(book.getName(), "name1", "Wrong name update");
        assertEquals(book.getAuthorName(), "author1", "Wrong authorName update");
        assertEquals(book.getPublishedDate(), "date1", "Wrong publishedDate update");
    }
}
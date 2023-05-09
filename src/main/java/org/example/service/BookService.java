package org.example.service;

import org.example.dao.BookRepository;
import org.example.dto.BookForm;
import org.example.exceptions.NotFound;
import org.example.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    @Autowired
    BookRepository bookRepo;

    public List<Book> getAllBooks() throws Exception{
        return bookRepo.findAll();
    }

    public Book addBook(BookForm bookForm) throws Exception{
        Book book = bookForm.initializeBook();
        bookRepo.save(book);
        return book;
    }

    public Book getBook(int id) throws Exception{
        Optional<Book> book = bookRepo.findById(id);
        if(book.isPresent()){
            return book.get();
        }
        else{
            throw new NotFound("Book with this id not found");
        }
    }

    public Book updateBook(int id, BookForm bookForm) throws Exception{
        Book _book = bookForm.initializeBook();
        Optional<Book> book = bookRepo.findById(id);
        if(book.isPresent()){
            book.get().update(_book);
            bookRepo.save(book.get());
            return _book;
        }
        else{
            throw new NotFound("Book with this id not found");
        }
    }

    public void deleteBook(int id) throws Exception{
        if(bookRepo.existsById(id)) {
            bookRepo.deleteById(id);
        }
        else {
            throw new NotFound("Book with this id not found");
        }
    }
}

package org.example.service;

import org.example.dao.BookRepository;
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

    public List<Book> getAllBooks(){
        return bookRepo.findAll();
    }

    public Book addBook(Book book){
        bookRepo.save(book);
        return book;
    }

    public Book getBook(int id) throws NotFound{
        Optional<Book> book = bookRepo.findById(id);
        if(book.isPresent()){
            return book.get();
        }
        else{
            throw new NotFound("Book with this id not found");
        }
    }

    public Book updateBook(int id, Book _book) throws NotFound{
        Optional<Book> book = bookRepo.findById(id);
        if(book.isPresent()){
            book.get().update(_book);
            bookRepo.save(book.get());
            return book.get();
        }
        else{
            throw new NotFound("Book with this id not found");
        }
    }

    public void deleteBook(int id) throws NotFound{
        if(bookRepo.existsById(id)) {
            bookRepo.deleteById(id);
        }
        else {
            throw new NotFound("Book with this id not found");
        }
    }
}

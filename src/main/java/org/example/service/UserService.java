package org.example.service;

import org.example.dao.*;
import org.example.exceptions.AlreadyExist;
import org.example.exceptions.NotFound;
import org.example.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class UserService {
    @Autowired
    BookRepository bookRepo;
    @Autowired
    UserRepository userRepo;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public List<Book> getAllBookInLibrary(String userEmail) throws Exception{
        User foundedUser = userRepo.findByEmail(userEmail);
        if (foundedUser == null) {
            logger.error("[UserService - getAllBookInLibrary] Authenticated user not found in database");
            throw new Exception("Authenticated user not found in database");
        }

        return foundedUser.getLibrary();
    }

    public Book getBookInLibrary(String userEmail, int bookId) throws Exception{
        User foundedUser = userRepo.findByEmail(userEmail);
        if (foundedUser == null) {
            logger.error("[UserService - getAllBookInLibrary] Authenticated user not found in database");
            throw new Exception("Authenticated user not found in database");
        }

        Book foundedBook = foundedUser.getBookFromLibrary(bookId);
        if(foundedBook == null){
            throw new NotFound("Book with this id not found in your library");
        }

        return foundedBook;
    }

    public void addBookToLibrary(String userEmail, int bookId) throws Exception{
        User foundedUser = userRepo.findByEmail(userEmail);
        if (foundedUser == null) {
            logger.error("[UserService - getAllBookInLibrary] Authenticated user not found in database");
            throw new Exception("Authenticated user not found in database");
        }

        if(foundedUser.getBookFromLibrary(bookId) != null){
            throw new AlreadyExist("Book with this id already exist in your library");
        }

        Optional<Book> foundedBook = bookRepo.findById(bookId);
        if(foundedBook.isEmpty()){
            throw new NotFound("Book with this id not found");
        }

        foundedUser.addBookToLibrary(foundedBook.get());
        userRepo.save(foundedUser);
    }

    public void deleteBookFromLibrary(String userEmail, int bookId) throws Exception{
        User foundedUser = userRepo.findByEmail(userEmail);
        if (foundedUser == null) {
            logger.error("[UserService - getAllBookInLibrary] Authenticated user not found in database");
            throw new Exception("Authenticated user not found in database");
        }

        if(foundedUser.getBookFromLibrary(bookId) == null){
            throw new NotFound("Book with this id not found in your library");
        }

        foundedUser.deleteBookFromLibrary(bookId);
        userRepo.save(foundedUser);
    }
}

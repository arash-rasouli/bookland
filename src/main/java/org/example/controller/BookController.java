package org.example.controller;

import jakarta.validation.Valid;

import org.example.dto.ResponseInfo;
import org.example.exceptions.NotFound;
import org.example.model.Book;
import org.example.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/book")
public class BookController {

    @Autowired
    BookService bookService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping("")
    public ResponseEntity<ResponseInfo> getAllBooks(){
        ResponseInfo responseInfo;
        try {
            List<Book> books = bookService.getAllBooks();
            responseInfo = new ResponseInfo(books, true);
            return ResponseEntity.ok(responseInfo);
        }
        catch (Exception ex){
            responseInfo = new ResponseInfo(null, false, "Internal Server Error");
            logger.error("[BookController-getAllBooks] Unhandled Exception: error message : {} , error stack : {}", ex.getMessage(), ex.getStackTrace());
            return new ResponseEntity<>(responseInfo, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<ResponseInfo> getBook(@PathVariable int id) throws NotFound {
        ResponseInfo responseInfo;
        try {
            Book book = bookService.getBook(id);
            responseInfo = new ResponseInfo(book, true);
            return ResponseEntity.ok(responseInfo);
        }
        catch (Exception ex){
            if (ex instanceof NotFound){throw (NotFound) ex;}

            responseInfo = new ResponseInfo(null, false, "Internal Server Error");
            logger.error("[BookController-getBook] Unhandled Exception: error message : {} , error stack : {}", ex.getMessage(), ex.getStackTrace());
            return new ResponseEntity<>(responseInfo, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("")
    public ResponseEntity<ResponseInfo> addBook(@Valid @RequestBody Book book){
        ResponseInfo responseInfo;
        try{
            Book _book = bookService.addBook(book);
            responseInfo = new ResponseInfo(_book, true, "Book Added Successfully");
            logger.info("Book with id {} added successfully", _book.getId());
            return new ResponseEntity<>(responseInfo, HttpStatus.CREATED);
        }
        catch (Exception ex){
            responseInfo = new ResponseInfo(null, false, "Internal Server Error");
            logger.error("[BookController-addBook] Unhandled Exception: error message : {} , error stack : {}", ex.getMessage(), ex.getStackTrace());
            return new ResponseEntity<>(responseInfo, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseInfo> updateBook(@PathVariable int id, @Valid @RequestBody Book book) throws NotFound{
        ResponseInfo responseInfo;
        try {
            Book _book = bookService.updateBook(id, book);
            responseInfo = new ResponseInfo(_book, true, "Book Updated Successfully");
            logger.info("Book with id {} updated successfully", _book.getId());
            return ResponseEntity.ok(responseInfo);
        }
        catch (Exception ex){
            if (ex instanceof NotFound){throw (NotFound) ex;}

            responseInfo = new ResponseInfo(null, false, "Internal Server Error");
            logger.error("[BookController-updateBook] Unhandled Exception: error message : {} , error stack : {}", ex.getMessage(), ex.getStackTrace());
            return new ResponseEntity<>(responseInfo, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseInfo> deleteBook(@PathVariable int id) throws NotFound{
        ResponseInfo responseInfo;
        try {
            bookService.deleteBook(id);
            responseInfo = new ResponseInfo(null, true, "Book Deleted Successfully");
            logger.info("Book with id {} deleted successfully", id);
            return ResponseEntity.ok(responseInfo);
        }
        catch (Exception ex){
            if (ex instanceof NotFound){throw (NotFound) ex;}

            responseInfo = new ResponseInfo(null, false, "Internal Server Error");
            logger.error("[BookController-deleteBook] Unhandled Exception: error message : {} , error stack : {}", ex.getMessage(), ex.getStackTrace());
            return new ResponseEntity<>(responseInfo, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

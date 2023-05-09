package org.example.controller;

import org.example.dto.ResponseInfo;
import org.example.exceptions.AlreadyExist;
import org.example.exceptions.NotFound;
import org.example.model.Book;
import org.example.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping("/library")
    public ResponseEntity<ResponseInfo> getAllBookInLibrary(@RequestAttribute(value = "userEmail") String userEmail){
        ResponseInfo responseInfo;
        try {
            List<Book> books = userService.getAllBookInLibrary(userEmail);
            responseInfo = new ResponseInfo(books, true, "");
            return ResponseEntity.ok(responseInfo);
        }
        catch (Exception ex){

            responseInfo = new ResponseInfo(null, false, "Internal Server Error");
            logger.error("Unhandled Exception: error message : {} , error stack : {}", ex.getMessage(), ex.getStackTrace());
            return new ResponseEntity<>(responseInfo, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/library/{id}")
    public ResponseEntity<ResponseInfo> getBookInLibrary(@RequestAttribute(value = "userEmail") String userEmail,
                                                         @PathVariable int id) throws NotFound{
        ResponseInfo responseInfo;
        try {
            Book book = userService.getBookInLibrary(userEmail, id);
            responseInfo = new ResponseInfo(book, true, "");
            return ResponseEntity.ok(responseInfo);
        }
        catch (Exception ex){
            if (ex instanceof NotFound) throw (NotFound) ex;

            responseInfo = new ResponseInfo(null, false, "Internal Server Error");
            logger.error("Unhandled Exception: error message : {} , error stack : {}", ex.getMessage(), ex.getStackTrace());
            return new ResponseEntity<>(responseInfo, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/library/{id}")
    public ResponseEntity<ResponseInfo> addBookToLibrary(@RequestAttribute(value = "userEmail") String userEmail,
                                                         @PathVariable int id) throws NotFound, AlreadyExist {
        ResponseInfo responseInfo;
        try {
            userService.addBookToLibrary(userEmail, id);
            responseInfo = new ResponseInfo(null, true, "Book Added To Library Successfully");
            return ResponseEntity.ok(responseInfo);
        }
        catch (Exception ex){
            if (ex instanceof NotFound) throw (NotFound) ex;
            if (ex instanceof AlreadyExist) throw (AlreadyExist) ex;

            responseInfo = new ResponseInfo(null, false, "Internal Server Error");
            logger.error("Unhandled Exception: error message : {} , error stack : {}", ex.getMessage(), ex.getStackTrace());
            return new ResponseEntity<>(responseInfo, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/library/{id}")
    public ResponseEntity<ResponseInfo> deleteBookFromLibrary(@RequestAttribute(value = "userEmail") String userEmail,
                                                         @PathVariable int id) throws NotFound, AlreadyExist {
        ResponseInfo responseInfo;
        try {
            userService.deleteBookFromLibrary(userEmail, id);
            responseInfo = new ResponseInfo(null, true, "Book Deleted From Library Successfully");
            return ResponseEntity.ok(responseInfo);
        }
        catch (Exception ex){
            if (ex instanceof NotFound) throw (NotFound) ex;
            if (ex instanceof AlreadyExist) throw (AlreadyExist) ex;

            responseInfo = new ResponseInfo(null, false, "Internal Server Error");
            logger.error("Unhandled Exception: error message : {} , error stack : {}", ex.getMessage(), ex.getStackTrace());
            return new ResponseEntity<>(responseInfo, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

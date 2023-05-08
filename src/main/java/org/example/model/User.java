package org.example.model;

import jakarta.persistence.*;

import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.example.utils.Util;

import java.util.*;

@Entity
@Table(name = "USER_")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotBlank(message = "userEmail can not be blank")
    @Email(message = "Wrong userEmail format")
    private String email;
    @NotBlank(message = "Password can not be blank")
    @Size(min = 4, message = "Password should contain at least 4 character")
    private String password;

    @ManyToMany
    @JoinTable(
            name = "user_book",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id"))
    List<Book> library;

    public User() {
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public void addBookToLibrary(Book book){
        library.add(book);
    }

    public Book getBookFromLibrary(int bookId){
        for(Book book : library){
            if (book.getId() == bookId) return book;
        }
        return null;
    }

    public void deleteBookFromLibrary(int bookId){
        library.removeIf(book -> book.getId() == bookId);
    }

    public boolean verifyPassword(String password){
        String passHash =  Util.hexToString(Util.getSHA(password));
        return this.password.equals(passHash);
    }

    public List<Book> getLibrary() {
        return library;
    }

    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
}

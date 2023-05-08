package org.example.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

//import java.time.LocalDate;
import java.time.LocalDate;
import java.util.List;

@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotBlank(message = "name can't be blank")
    private String name;
    @NotBlank(message = "authorName can't be blank")
    private String authorName;
    @NotBlank(message = "publishedDate can't be null")
    @Pattern(regexp = "^\\d{4}\\-(0?[1-9]|1[012])\\-(0?[1-9]|[12][0-9]|3[01])$", message = "Invalid Date Format(format = yyyy-mm-dd)")
    private String publishedDate;

    @ManyToMany(mappedBy = "library")
    List<User> users;

    public void update(Book _book){
        name = _book.getName();
        authorName = _book.getAuthorName();
//        publishedDate = _book.getPublishedDate();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getAuthorName() {
        return authorName;
    }
    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getPublishedDate() {
        return publishedDate;
    }
    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }
}

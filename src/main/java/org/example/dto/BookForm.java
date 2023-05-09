package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.example.model.Book;

public class BookForm {

    @NotBlank(message = "name can't be blank")
    private String name;
    @NotBlank(message = "authorName can't be blank")
    private String authorName;
    @NotBlank(message = "publishedDate can't be null")
    @Pattern(regexp = "^\\d{4}\\-(0?[1-9]|1[012])\\-(0?[1-9]|[12][0-9]|3[01])$", message = "Invalid Date Format(format = yyyy-mm-dd)")
    private String publishedDate;

    public Book initializeBook(){
        return new Book(name, authorName, publishedDate);
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

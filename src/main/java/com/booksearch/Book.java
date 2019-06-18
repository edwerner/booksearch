package com.booksearch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Book {

    private String isbn;
    private String id;
    private String title;
    private String author;
    private String language;
    private String rating;
    private String smImage;
    private String lgImage;
    private String year;

    public String getId() {
        return id;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getLgImage() {
        return lgImage;
    }

    public void setLgImage(String lgImage) {
        this.lgImage = lgImage;
    }

    public String getSmImage() {
        return smImage;
    }

    public void setSmImage(String smImage) {
        this.smImage = smImage;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
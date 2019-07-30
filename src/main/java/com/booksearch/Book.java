package com.booksearch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;
import org.hibernate.search.bridge.builtin.IntegerBridge;
import org.springframework.data.mongodb.core.mapping.Document;

import org.bson.types.ObjectId;

@Document
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Indexed
public class Book {

	@DocumentId
	@FieldBridge(impl = IntegerBridge.class)
    private ObjectId id;
    @Field(index=Index.YES, analyze=Analyze.YES, store=Store.NO)
    private String isbn;
    @Field(index=Index.YES, analyze=Analyze.YES, store=Store.NO)
    private String title;
    @Field(index=Index.YES, analyze=Analyze.YES, store=Store.NO)
    private String author;
    @Field(index=Index.YES, analyze=Analyze.YES, store=Store.NO)
    private String language;
    @Field(index=Index.YES, analyze=Analyze.YES, store=Store.NO)
    private String rating;
    private String smImage;
    private String lgImage;
    @Field(index=Index.YES, analyze=Analyze.YES, store=Store.NO)
    private String year;

    public ObjectId getId() {
        return id;
    }
    
    public void setId(ObjectId id) {
        this.id = id;
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
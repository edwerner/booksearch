package com.booksearch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;
import org.hibernate.search.bridge.builtin.IntegerBridge;
import org.hibernate.search.bridge.builtin.StringBridge;
import org.springframework.data.mongodb.core.mapping.Document;

import org.bson.types.ObjectId;

@Entity
@Indexed
@Table(name = "book")
@Document
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Book {

	@Id
	@GeneratedValue
	private String id;

	@Field
	private String isbn;

	@Field
	private String title;

	@Field
	private String author;

	@Field
	private String language;

	@Field
	private String rating;

	private String smImage;
	private String lgImage;

	@Field
	private String year;

	public String getId() {
		return id;
	}

	public void setId(String id) {
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
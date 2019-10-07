package com.booksearch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Id;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Book {

	@Id
	private String id;
	private String isbn;
	private String title;
	private String author;
	private String language;
	private String rating;
	private String year;
	private String smImage;
	private String lgImage;
	
	public Book(String isbn, String title, String author, 
			String language, String rating, String year,
			String smImage, String lgImage) {
		this.isbn = isbn;
		this.title = title;
		this.author = author;
		this.language = language;
		this.rating = rating;
		this.year = year;
		this.smImage = smImage;
		this.lgImage = lgImage;
	}
	
	@Field("id")
	protected void setId(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}

	public String getIsbn() {
		return isbn;
	}

	@Field("isbn")
	protected void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getTitle() {
		return title;
	}

	@Field("title")
	protected void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	@Field("author")
	protected void setAuthor(String author) {
		this.author = author;
	}

	public String getLanguage() {
		return language;
	}

	@Field("language")
	protected void setLanguage(String language) {
		this.language = language;
	}

	public String getRating() {
		return rating;
	}

	@Field("rating")
	protected void setRating(String rating) {
		this.rating = rating;
	}

	public String getYear() {
		return year;
	}

	@Field("year")
	protected void setYear(String year) {
		this.year = year;
	}

	public String getSmImage() {
		return smImage;
	}

	public void setSmImage(String smImage) {
		this.smImage = smImage;
	}

	public String getLgImage() {
		return lgImage;
	}

	public void setLgImage(String lgImage) {
		this.lgImage = lgImage;
	}
}
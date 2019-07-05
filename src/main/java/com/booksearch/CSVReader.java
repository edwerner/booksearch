package com.booksearch;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import reactor.core.publisher.Flux;

public class CSVReader {

	public CSVReader() {}
	
	public static void saveBooks(BookRepository bookRepository) {
		String csvFile = "books.csv";
		BufferedReader br = null;
		String line = "";
		String splitOn = ",";

		try {
			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {

				String[] books = line.split(splitOn);

				String isbn = books[5];
				String author = books[7];
				String year = books[8];
				String title = books[10];
				String language = books[11];
				String rating = books[12];
				String lgImage = books[21];
				String smImage = books[22];

				Book book = new Book();
				book.setTitle(title);
				book.setAuthor(author);
				book.setIsbn(isbn);
				book.setLanguage(language);
				book.setRating(rating);
				book.setYear(year);
				book.setLgImage(lgImage);
				book.setSmImage(smImage);
				bookRepository.saveAll(Flux.just(book)).subscribe();
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}